package com.kontrerd.mtransfer.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class AccountHistoricItem {
    private Long accountId;
    private Operation operation;
    private BigDecimal balanceAfter;
    private Date date;
}
