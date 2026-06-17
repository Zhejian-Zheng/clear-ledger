package com.clearledger.clearledger.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @PostMapping
    public String createTransaction() {
        return "新增交易";
    }

    @GetMapping
    public String getTransactions() {
        return "查看交易列表";
    }

    @GetMapping("/{id}")
    public String getTransaction(@PathVariable Long id) {
        return "查看单笔交易: " + id;
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        return "删除交易: " + id;
    }
}
