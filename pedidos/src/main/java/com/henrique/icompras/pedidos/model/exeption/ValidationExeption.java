package com.henrique.icompras.pedidos.model.exeption;

import lombok.Getter;

@Getter
public class ValidationExeption extends RuntimeException {

    private String field;
    private String message;

    public ValidationExeption(String field, String message) {
        super(message);
        this.field = field;
        this.message = message;
    }

}
