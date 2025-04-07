package com.alibou.security.feature.transaction.dao;

import com.alibou.security.feature.transaction.model.MovieTransaction;

import java.util.List;

public interface TransactionDao {

    Integer saveTransaction(MovieTransaction transaction);

    MovieTransaction findByPaymentId (String paymentId);

    MovieTransaction getTransactionByUserId(String userId);

    List<MovieTransaction> getAllTransactionByUserId(String userId, Integer page, Integer pageSize);
}
