package com.zam.security.services;

import com.stripe.exception.StripeException;
import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.response.SaleDetailResponse;
import com.zam.security.payload.response.SaleResponse;
import jakarta.mail.MessagingException;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.List;

public interface SaleService {

    public MessageResponse makePurchase() throws JRException, IOException, MessagingException, StripeException;
    public List<SaleResponse> getPurchases();
    public List<SaleDetailResponse> getPurchaseDetails(Integer saleId);

}
