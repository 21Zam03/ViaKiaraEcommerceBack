package com.zam.security.services.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.zam.security.entities.*;
import com.zam.security.exceptions.NotFoundException;
import com.zam.security.payload.request.PaymentIntentRequest;
import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.response.SaleDetailResponse;
import com.zam.security.payload.response.SaleResponse;
import com.zam.security.repositories.*;
import com.zam.security.services.EmailService;
import com.zam.security.services.PaymentService;
import com.zam.security.services.ReportService;
import com.zam.security.services.SaleService;
import jakarta.mail.MessagingException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final ShopCartRepository shopCartRepository;
    private final ShopCartDetailRepository shopCartDetailRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final EmailService emailService;
    private final ReportService reportService;
    private final ImageRepository imageRepository;
    private final PaymentService paymentService;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, ClientRepository clientRepository,
                           ShopCartRepository shopCartRepository, ShopCartDetailRepository shopCartDetailRepository,
                           SaleDetailRepository saleDetailRepository, EmailService emailService, ImageRepository imageRepository,
                           ReportService reportService, PaymentService paymentService) {
        this.saleRepository = saleRepository;
        this.clientRepository = clientRepository;
        this.shopCartRepository = shopCartRepository;
        this.shopCartDetailRepository = shopCartDetailRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.emailService = emailService;
        this.imageRepository = imageRepository;
        this.reportService = reportService;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public MessageResponse makePurchase() throws JRException, IOException, MessagingException, StripeException {

        ClientEntity loggedClient = this.getLoggedClient();
        ShopCartEntity shopCart = shopCartRepository.findByClient(loggedClient).orElseThrow(() -> {
            return new RuntimeException("Shop Cart not found");
        });

        List<ShopCartDetailEntity> shopCartDetailEntities = shopCartDetailRepository.findByShopCart(shopCart);
        Double total = shopCartDetailEntities.stream().mapToDouble(shopDetail -> shopDetail.getQuantity()*shopDetail.getProductEntity().getProductPrice()).sum();

        /*Generamos el paymentIntent*/
        PaymentIntentRequest paymentIntentRequest = PaymentIntentRequest.builder()
                .amount(total.longValue()*100)
                .currency("pen")
                .description("Sale")
                .build();

        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentIntentRequest);
        paymentService.confirmPaymentIntent(paymentIntent.getId());

        /*Creamos la venta*/
        SaleEntity saleEntity = SaleEntity.builder()
                .client(loggedClient)
                .purchaseDate(LocalDate.now())
                .total(total)
                .build();

        SaleEntity saleCreated = saleRepository.save(saleEntity);

        for (ShopCartDetailEntity shopCartDetailEntity : shopCartDetailEntities) {
            SaleDetailEntity saleDetailEntity = SaleDetailEntity.builder()
                    .sizeNumber(shopCartDetailEntity.getSizeNumber())
                    .saleEntity(saleCreated)
                    .product(shopCartDetailEntity.getProductEntity())
                    .colorName(shopCartDetailEntity.getColor())
                    .quantity(shopCartDetailEntity.getQuantity())
                    .price(shopCartDetailEntity.getProductEntity().getProductPrice())
                    .build();
            saleDetailRepository.save(saleDetailEntity);
        }
        shopCartDetailRepository.deleteByShopCart(shopCart);

        /*Generamos el reporte*/
        File file = reportService.generateReport(saleCreated.getSaleId().toString(), saleCreated.getPurchaseDate().toString(), saleCreated.getTotal(),
                saleCreated.getClient().getFirstName()+" "+saleCreated.getClient().getLastName(), String.valueOf(shopCartDetailEntities.size()));

        /*Enviamos el correo electronico*/
        emailService.sendEmailWithFile(
                loggedClient.getUser().getEmail(),
                "¡Recibo de compra \uD83C\uDF89!", "Gracias! por tu compra en Via Kiara \uD83C\uDF1F, " +
                        "Nos complace informarte que tu compra ha sido procesada con éxito. " +
                        "A continuación te compartimos los detalles de tu pedido",
                file);
        return new MessageResponse("Purchase successful");

    }

    @Override
    public List<SaleResponse> getPurchases() {
        ClientEntity client = getLoggedClient();
        List<SaleEntity> listSales = saleRepository.findByClientClientId(client.getClientId());
        List<SaleResponse> listSaleResponse = new ArrayList<>();
        for (SaleEntity listSale : listSales) {
            SaleResponse saleResponse = SaleResponse.builder()
                    .saleId(listSale.getSaleId())
                    .saleDate(listSale.getPurchaseDate())
                    .total(listSale.getTotal())
                    .build();
            listSaleResponse.add(saleResponse);
        }
        return listSaleResponse;
    }

    @Override
    public List<SaleDetailResponse> getPurchaseDetails(Integer saleId) {

        SaleEntity saleEntity = saleRepository.findById(saleId).orElseThrow(() -> {
            return new NotFoundException("Sale not found");
        });

        List<SaleDetailEntity> saleDetailEntityList = saleDetailRepository.findBySaleEntitySaleId(saleId);
        List<SaleDetailResponse> listSaleDetailResponse = new ArrayList<>();
        for (SaleDetailEntity saleDetailEntity : saleDetailEntityList) {
            SaleDetailResponse saleDetailResponse = SaleDetailResponse.builder()
                    .saleDetailId(saleDetailEntity.getSaleDetailId())
                    .productName(saleDetailEntity.getProduct().getProductName())
                    .quantity(saleDetailEntity.getQuantity())
                    .price(saleDetailEntity.getProduct().getProductPrice())
                    .colorName(saleDetailEntity.getColorName())
                    .productImage(imageRepository.findTop1ByProductProductIdAndColorColorName
                            (saleDetailEntity.getProduct().getProductId(), saleDetailEntity.getColorName()).getImageUrl())
                    .sizeNumber(saleDetailEntity.getSizeNumber())
                    .build();
            listSaleDetailResponse.add(saleDetailResponse);
        }
        return listSaleDetailResponse;
    }

    public ClientEntity getLoggedClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return clientRepository.findByUserEmail(authentication.getName());
    }

}
