package com.clearledger.clearledger.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryResponse {

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal balance;
}
