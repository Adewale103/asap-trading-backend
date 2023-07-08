package com.twinkles.asaptrading.repository;

import com.twinkles.asaptrading.entity.Transaction;
import com.twinkles.asaptrading.enums.TransactionStatus;
import com.twinkles.asaptrading.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> getTransactionsByTransactionType(TransactionType transactionType);
    List<Transaction> getTransactionsByTransactionStatus(TransactionStatus transactionStatus);
    List<Transaction> getTransactionsByTransactionStatusAndTransactionType(TransactionStatus transactionStatus, TransactionType transactionType);

}
