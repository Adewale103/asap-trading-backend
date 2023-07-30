package com.twinkles.asaptrading.service.transaction;

import com.twinkles.asaptrading.dto.GenericResponse;
import com.twinkles.asaptrading.dto.request.BuyCoinRequest;
import com.twinkles.asaptrading.dto.request.SellCoinRequest;
import com.twinkles.asaptrading.entity.Transaction;
import com.twinkles.asaptrading.entity.User;


public interface TransactionService {
    GenericResponse updateTransactionStatus(String transactionId, String status);
    GenericResponse getPendingBuyTransactions();
    GenericResponse getPendingSellTransactions();
    Transaction buyCoin(BuyCoinRequest buyCoinRequest, User user);
    Transaction sellCoin(SellCoinRequest sellCoinRequest, User user);
}
