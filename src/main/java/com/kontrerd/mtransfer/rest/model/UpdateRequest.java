package com.kontrerd.mtransfer.rest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class UpdateRequest {
    private Long id;
    private BigDecimal amount;
}
