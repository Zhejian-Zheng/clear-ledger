package com.clearledger.clearledger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SummaryController {

    @GetMapping("/summary")
    public String getSummary() {
        return "查看余额汇总";
    }
}