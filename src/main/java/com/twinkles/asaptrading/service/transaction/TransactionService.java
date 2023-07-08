package com.twinkles.asaptrading.service.transaction;

import com.twinkles.asaptrading.dto.GenericResponse;
import com.twinkles.asaptrading.dto.request.BuyCoinRequest;
import com.twinkles.asaptrading.dto.request.SellCoinRequest;
import com.twinkles.asaptrading.entity.User;


public interface TransactionService {
    GenericResponse updateTransactionStatus(String transactionId, String status);
    GenericResponse getPendingBuyTransactions();
    GenericResponse getPendingSellTransactions();
    GenericResponse buyCoin(BuyCoinRequest buyCoinRequest, User user);
    GenericResponse sellCoin(SellCoinRequest sellCoinRequest, User user);
}
