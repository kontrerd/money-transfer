package com.kontrerd.mtransfer.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorEntity {
    private int code;
    private String message;
}
