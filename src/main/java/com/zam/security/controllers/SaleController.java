package com.zam.security.controllers;

import com.stripe.exception.StripeException;
import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.response.SaleDetailResponse;
import com.zam.security.payload.response.SaleResponse;
import com.zam.security.services.SaleService;
import jakarta.mail.MessagingException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(SaleController.API_PATH)
public class SaleController {

    public static final String API_PATH = "/api/sale";

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> getPurchases(){
        return new ResponseEntity<>(saleService.getPurchases(), HttpStatus.OK);
    }

    @GetMapping("/details/{saleId}")
    public ResponseEntity<List<SaleDetailResponse>> getPurchaseDetail(@PathVariable Integer saleId){
        return new ResponseEntity<>(saleService.getPurchaseDetails(saleId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> makePurchase() throws JRException, MessagingException, IOException, StripeException {
        return new ResponseEntity<>(saleService.makePurchase(), HttpStatus.CREATED);
    }



}
