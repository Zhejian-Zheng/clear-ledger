package com.clearledger.clearledger.repository;

import com.clearledger.clearledger.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
