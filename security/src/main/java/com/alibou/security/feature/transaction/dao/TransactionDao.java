package com.alibou.security.feature.transaction.dao;

import com.alibou.security.feature.transaction.model.MovieTransaction;

public interface TransactionDao {

    Integer saveTransaction(MovieTransaction transaction);

    MovieTransaction findByPaymentId (String paymentId);

    MovieTransaction getTransactionByUserId(String userId);
}
