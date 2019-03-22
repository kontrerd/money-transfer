package com.kontrerd.mtransfer.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    private Long id;
    private BigDecimal amount;
}
