package com.zam.security.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.zam.security.payload.request.PaymentIntentRequest;
import com.zam.security.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PaymentController.API_PATH)
public class PaymentController {

    public static final String API_PATH = "/api/payment";

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentIntentRequest paymentIntentRequest) throws StripeException {
        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentIntentRequest);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<>(paymentStr, HttpStatus.CREATED);
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<?> confirmPaymentIntent(@PathVariable("id") String paymentId) throws StripeException {
        PaymentIntent paymentIntent = paymentService.confirmPaymentIntent(paymentId);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<>(paymentStr, HttpStatus.CREATED);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelPaymentIntent(@PathVariable("id") String paymentId) throws StripeException {
        PaymentIntent paymentIntent = paymentService.cancelPaymentIntent(paymentId);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<>(paymentStr, HttpStatus.CREATED);
    }

}
