package com.clearledger.clearledger.controller;

import com.clearledger.clearledger.dto.SummaryResponse;
import com.clearledger.clearledger.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SummaryController {

    private final TransactionService transactionService;

    public SummaryController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/summary")
    public SummaryResponse getSummary() {
        return transactionService.getSummary();
    }
}
