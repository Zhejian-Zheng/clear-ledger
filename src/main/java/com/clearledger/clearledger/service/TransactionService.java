package com.clearledger.clearledger.service;

import com.clearledger.clearledger.dto.CreateTransactionRequest;
import com.clearledger.clearledger.dto.SummaryResponse;
import com.clearledger.clearledger.dto.TransactionResponse;
import com.clearledger.clearledger.entity.Transaction;
import com.clearledger.clearledger.entity.TransactionType;
import com.clearledger.clearledger.exception.ResourceNotFoundException;
import com.clearledger.clearledger.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setCategory(request.getCategory());
        transaction.setTransactionDate(request.getTransactionDate());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return toResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));

        return toResponse(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found: " + id);
        }

        transactionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SummaryResponse getSummary() {
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        for (Transaction transaction : transactionRepository.findAll()) {
            if (transaction.getType() == TransactionType.INCOME) {
                income = income.add(transaction.getAmount());
            } else if (transaction.getType() == TransactionType.EXPENSE) {
                expense = expense.add(transaction.getAmount());
            }
        }

        SummaryResponse response = new SummaryResponse();
        response.setTotalIncome(income);
        response.setTotalExpense(expense);
        response.setBalance(income.subtract(expense));
        return response;
    }

    private TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setDescription(transaction.getDescription());
        response.setCategory(transaction.getCategory());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }
}
