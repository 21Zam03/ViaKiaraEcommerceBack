package com.zam.security.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentIntentRequest {

    private String description;
    private Long amount;
    private String currency;
    private String cardNumber;
    private Long cardExpiryMonth;
    private Long cardExpiryYear;
    private String cvcCard;

}
