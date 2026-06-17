package com.clearledger.clearledger.dto;

import com.clearledger.clearledger.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTransactionRequest {
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private String category;
    private LocalDate transactionDate;
}