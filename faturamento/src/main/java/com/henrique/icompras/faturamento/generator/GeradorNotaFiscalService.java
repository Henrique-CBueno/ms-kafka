package com.henrique.icompras.faturamento.generator;

import com.henrique.icompras.faturamento.bucket.BucketFile;
import com.henrique.icompras.faturamento.bucket.service.BucketService;
import com.henrique.icompras.faturamento.model.Pedido;
import com.henrique.icompras.faturamento.publisher.FaturamentoPublisher;
import com.henrique.icompras.faturamento.service.NotaFiscalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class GeradorNotaFiscalService {

    private final NotaFiscalService notaFiscalService;
    private final BucketService bucketService;
    private final FaturamentoPublisher faturamentoPublisher;

    public void gerar(Pedido pedido) {
        log.info("GERANDO A NOTA FISCAL PARA O PEDIDO: {}", pedido.codigo());

        try {

            byte[] byteArray = notaFiscalService.gerarNota(pedido);

            String nomeArquivo = String.format("notaFiscal_pedido_%d.pdf", pedido.codigo());

            BucketFile file = new BucketFile(nomeArquivo,
                    new ByteArrayInputStream(byteArray),
                    MediaType.APPLICATION_PDF,
                    (long) byteArray.length);

            bucketService.upload(file);

            String url = bucketService.getUrl(nomeArquivo);
            faturamentoPublisher.publicar(pedido, url);

            log.info("NOTA GERADA COM SUCESSO, NOME DO ARQUIVO: {}", nomeArquivo);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
