package com.kontrerd.mtransfer.rest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TransferRequest {
    private Long srcId;
    private Long destinationId;
    private BigDecimal amount;
}