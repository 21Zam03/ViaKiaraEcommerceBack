package com.zam.security.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.zam.security.payload.request.PaymentIntentRequest;

public interface PaymentService {

    public PaymentIntent createPaymentIntent(PaymentIntentRequest paymentIntentRequest) throws StripeException;
    public PaymentIntent confirmPaymentIntent(String paymentId) throws StripeException;
    public PaymentIntent cancelPaymentIntent(String paymentId) throws StripeException;

}
