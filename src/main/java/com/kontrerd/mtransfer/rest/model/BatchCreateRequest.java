package com.kontrerd.mtransfer.rest.model;

import com.kontrerd.mtransfer.core.model.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BatchCreateRequest {
    private List<Account> accounts;
}
