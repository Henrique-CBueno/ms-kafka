package com.henrique.icompras.faturamento.bucket.controller;

import com.henrique.icompras.faturamento.bucket.BucketFile;
import com.henrique.icompras.faturamento.bucket.service.BucketService;
import com.henrique.icompras.faturamento.service.NotaFiscalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("buckets")
@RequiredArgsConstructor
@Slf4j
public class BucketController {

    private final BucketService bucketService;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            log.info("FAZENDO O UPLOAD DO ARQUIVO {} PARA O BUCKET", file.getOriginalFilename());

            MediaType type = MediaType.parseMediaType(file.getContentType());
            BucketFile bucketFile = new BucketFile(file.getOriginalFilename(), is, type, file.getSize());
            bucketService.upload(bucketFile);

            log.info("ARQUIVO {} ENVIADO PARA O BUCKET COM SUCESSO", file.getOriginalFilename());
            return ResponseEntity.ok("ARQUIVO ENVIADO COM SUCESSO!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("erro ao enviar o arquivo: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<String> getArchiveUrl(@RequestParam String filename) {

        try {
            String url = bucketService.getUrl(filename);
            return ResponseEntity.ok(url);

        }  catch (Exception e) {
            return ResponseEntity.status(500).body("erro ao receber a url do arquivo: " + e.getMessage());
        }
    }
}
