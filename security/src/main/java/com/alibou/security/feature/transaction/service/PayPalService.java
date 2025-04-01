package com.alibou.security.feature.transaction.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PayPalService {

    Payment createPayment(Double total, String currency, String description, String cancelUrl, String successUrl) throws PayPalRESTException;

    Payment executePayment(String paymentId, String payerId, String userId, String packageId) throws PayPalRESTException ;

}
