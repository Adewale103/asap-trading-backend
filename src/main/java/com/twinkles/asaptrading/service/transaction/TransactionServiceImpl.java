package com.twinkles.asaptrading.service.transaction;

import com.twinkles.asaptrading.dto.*;
import com.twinkles.asaptrading.dto.request.BuyCoinRequest;
import com.twinkles.asaptrading.dto.request.SellCoinRequest;
import com.twinkles.asaptrading.entity.AccountDetails;
import com.twinkles.asaptrading.entity.Transaction;
import com.twinkles.asaptrading.entity.User;
import com.twinkles.asaptrading.enums.AccountDetailsType;
import com.twinkles.asaptrading.enums.TransactionStatus;
import com.twinkles.asaptrading.enums.TransactionType;
import com.twinkles.asaptrading.exception.AsapTradingException;
import com.twinkles.asaptrading.repository.AccountDetailsRepository;
import com.twinkles.asaptrading.repository.TransactionRepository;
import com.twinkles.asaptrading.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;

    @Override
    public GenericResponse updateTransactionStatus(String transactionId, String status) {
        Transaction transaction = transactionRepository.findById(Long.parseLong(transactionId)).orElseThrow(() -> new AsapTradingException("No transaction found for Id supplied",404));
        transaction.setTransactionStatus(TransactionStatus.valueOf(status));
        transactionRepository.save(transaction);
        return GenericResponse.builder().message("transaction status successfully updated").successful(true).build();
    }

    @Override
    public GenericResponse getPendingBuyTransactions() {
        List<Transaction> transactionList = transactionRepository.getTransactionsByTransactionStatusAndTransactionType(TransactionStatus.PENDING, TransactionType.BUY);
        if(transactionList == null || transactionList.size() < 1){
            throw new AsapTradingException("transaction list is empty",400);
        }
        List<TransactionDto> transactionDtoList = transactionList.stream().map(transaction -> {
            TransactionDto transactionDto  = new TransactionDto();
            BeanUtils.copyProperties(transaction,transactionDto);
            return transactionDto;
        }).toList();
        return new GenericResponse(true,"data successfully retrieved",transactionDtoList);
    }

    @Override
    public GenericResponse getPendingSellTransactions() {
        List<Transaction> transactionList = transactionRepository.getTransactionsByTransactionStatusAndTransactionType(TransactionStatus.PENDING, TransactionType.BUY);
        if(transactionList == null || transactionList.isEmpty()){
            throw new AsapTradingException("transaction list is empty",400);
        }
        List<TransactionDto> transactionDtoList = transactionList.stream().map(transaction -> {
            TransactionDto transactionDto  = new TransactionDto();
            BeanUtils.copyProperties(transaction,transactionDto);
            return transactionDto;
        }).toList();
        return new GenericResponse(true,"data successfully retrieved",transactionDtoList);
    }

    @Override
    public GenericResponse buyCoin(BuyCoinRequest buyCoinRequest, User user) {
        Transaction transaction  = new Transaction();
        TransactionDto transactionDto = new TransactionDto();
        VerifyTransactionResponse payStackResponse = confirmPayment(buyCoinRequest.getPaymentReferenceId());
        if (payStackResponse == null || payStackResponse.getStatus().equals("false")) {
            throw new AsapTradingException("An error occurred while verifying payment",400);
        }
        else if (payStackResponse.getData().getStatus().equals("success")) {
            transaction.setTransactionType(TransactionType.BUY);
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            transaction.setAmount(buyCoinRequest.getTotalAmount());
            transaction.setUser(user);
            transactionRepository.save(transaction);
            BeanUtils.copyProperties(transaction,transactionDto);
        }
        return new GenericResponse(true,"Payment has been confirmed. Transaction awaiting approval from admin",transactionDto);
    }

    @Override
    public GenericResponse sellCoin(SellCoinRequest sellCoinRequest, User user) {
     Transaction transaction  = new Transaction();
     TransactionDto transactionDto = new TransactionDto();
     transaction.setTransactionType(TransactionType.SELL);
     transaction.setTransactionStatus(TransactionStatus.PENDING);
     transaction.setUser(user);
     transaction.setAmount(sellCoinRequest.getTotalAmount());
     transactionRepository.save(transaction);
     BeanUtils.copyProperties(transaction,transactionDto);
     transactionDto.setAccountDetails(user.getAccountDetailsList().stream().toList().get(0));
     return new GenericResponse(true,"Transaction awaiting approval from admin",transactionDto);
    }


    private VerifyTransactionResponse confirmPayment(String paymentReference) {
        log.info("reference2---->{}", paymentReference);
        RestTemplate restTemplate = new RestTemplate();
        String url = System.getenv("PAYSTACK_URL") + paymentReference;
        log.info("url is--->{}", url);
        String key = System.getenv("PAYSTACK_KEY");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + key);
        HttpEntity<WalletTransactionRequest> request = new HttpEntity<>(null, headers);
        ResponseEntity<VerifyTransactionResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, VerifyTransactionResponse.class);
        return response.getBody();
    }
}
