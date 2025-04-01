package com.alibou.security.feature.transaction.service;

import com.alibou.security.feature.transaction.dao.TransactionDao;
import com.alibou.security.feature.transaction.model.MovieTransaction;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class PayPalServiceImpl implements PayPalService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Autowired
    private TransactionDao transactionDao;

    @Override
    public Payment createPayment(Double total, String currency, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        APIContext apiContext = new APIContext(clientId, clientSecret, mode);
        return payment.create(apiContext);
    }

    @Override
    public Payment executePayment(String paymentId, String payerId, String userId, String packageId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        APIContext apiContext = new APIContext(clientId, clientSecret, mode);

        try {
            // Thực thi thanh toán
            Payment executedPayment = payment.execute(apiContext, paymentExecution);
            log.info("Payment {} executed successfully with state: {}", paymentId, executedPayment.getState());

            if (!executedPayment.getState().equals("approved")) {
                log.warn("Payment {} not approved by PayPal", paymentId);
                return executedPayment; // Trả về để controller xử lý tiếp
            }

            // Lưu giao dịch vào database (không kiểm tra paymentId)
            MovieTransaction transaction = new MovieTransaction();
            transaction.setUserId(userId);
            transaction.setPackageId(packageId);
            transaction.setAmount(Double.valueOf(executedPayment.getTransactions().get(0).getAmount().getTotal()));
            transaction.setCurrency("USD");
            transaction.setTransactionDate(new Date());
            transaction.setPaymentMethod("PayPal");
            transaction.setStatus("completed");
            transaction.setPaymentId(paymentId);
            transactionDao.saveTransaction(transaction);
            log.info("Transaction saved for payment {} and user {}", paymentId, userId);

            return executedPayment;
        } catch (PayPalRESTException e) {
            if (e.getMessage().contains("PAYMENT_ALREADY_DONE")) {
                log.info("Payment {} already completed by PayPal", paymentId);
                throw new PayPalRESTException("PAYMENT_ALREADY_DONE: Thanh toán đã được thực hiện trước đó!");
            }
            log.error("Error executing payment {}: {}", paymentId, e.getMessage());
            throw e; // Ném lại lỗi khác để controller xử lý
        }
    }
}