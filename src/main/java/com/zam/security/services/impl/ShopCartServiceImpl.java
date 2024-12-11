package com.zam.security.services.impl;

import com.zam.security.entities.*;
import com.zam.security.exceptions.NotFoundException;
import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.response.ProductCartResponse;
import com.zam.security.repositories.*;
import com.zam.security.services.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class ShopCartServiceImpl implements ShopCartService {

    private final ClientRepository clientRepository;
    private final ShopCartRepository shopCartRepository;
    private final ShopCartDetailRepository shopCartDetailRepository;
    private final ProductRepository productRepository;
    private final DiscountCodeRepository discountCodeRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public ShopCartServiceImpl(ClientRepository clientRepository, ShopCartRepository shopCartRepository
    , ShopCartDetailRepository shopCartDetailRepository, ProductRepository productRepository,
                               DiscountCodeRepository discountCodeRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.clientRepository = clientRepository;
        this.shopCartRepository = shopCartRepository;
        this.shopCartDetailRepository = shopCartDetailRepository;
        this.productRepository = productRepository;
        this.discountCodeRepository = discountCodeRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public MessageResponse addProductToCart(Integer productId, Integer Quantity, String sizeNumber, String color) {
        ClientEntity clientLogged = this.getLoggedClient();

        ShopCartEntity shopCart = shopCartRepository.findByClient(clientLogged).orElseThrow(() -> {
            return new RuntimeException("Shop Cart not found");
        });

        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> {
            return new RuntimeException("Product not found");
        });

        if(shopCartDetailRepository.existsByShopCartAndProductEntity(shopCart, productEntity)) {
            return new MessageResponse("Product is already in the shop cart");
        }

        ShopCartDetailEntity shopCartDetailEntity = ShopCartDetailEntity.builder()
                .shopCart(shopCart)
                .productEntity(productEntity)
                .quantity(Quantity)
                .sizeNumber(sizeNumber)
                .color(color)
                .build();
        shopCartDetailRepository.save(shopCartDetailEntity);
        return new MessageResponse("Product added to shop cart");
    }

    @Override
    @Transactional
    public MessageResponse removeProductFromCart(Integer productId) {
        ClientEntity clientLogged = this.getLoggedClient();

        ShopCartEntity shopCart = shopCartRepository.findByClient(clientLogged).orElseThrow(() -> {
            return new RuntimeException("Shop Cart not found");
        });

        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> {
            return new RuntimeException("Product not found");
        });

        if(!shopCartDetailRepository.existsByShopCartAndProductEntity(shopCart, productEntity)) {
            return new MessageResponse("Product to delete is not in the shop cart");
        }

        shopCartDetailRepository.deleteByShopCartAndProductEntity(shopCart, productEntity);
        return new MessageResponse("Product removed from shop cart");
    }

    @Override
    public MessageResponse addDiscountToCart(String discountCode) {
        DiscountCodeEntity discountCodeEntity = discountCodeRepository.findByDiscountCode(discountCode).orElseThrow(() -> {
            return new NotFoundException("Discount code was not found");
        });

        ClientEntity clientLogged = this.getLoggedClient();

        ShopCartEntity shopCart = shopCartRepository.findByClient(clientLogged).orElseThrow(() -> {
            return new RuntimeException("Shop Cart not found");
        });
        shopCart.setDiscount(discountCodeEntity.getDiscountValue());
        shopCartRepository.save(shopCart);
        return new MessageResponse("Discount added to shop cart");
    }

    @Override
    public List<ProductCartResponse> getProductsInCart() {

        ClientEntity clientLogged = this.getLoggedClient();

        ShopCartEntity shopCart = shopCartRepository.findByClient(clientLogged).orElseThrow(() -> {
            return new RuntimeException("Shop Cart not found");
        });

        List<ShopCartDetailEntity> shopCartDetailEntities = shopCartDetailRepository.findByShopCart(shopCart);
        List<ProductCartResponse> productCartResponses = new ArrayList<>();

        for (ShopCartDetailEntity shopCartDetailEntity : shopCartDetailEntities) {
            ProductCartResponse productCartResponse = ProductCartResponse.builder()
                    .productId(shopCartDetailEntity.getProductEntity().getProductId())
                    .productName(shopCartDetailEntity.getProductEntity().getProductName())
                    .productDescription(shopCartDetailEntity.getProductEntity().getProductDescription())
                    .productPrice(shopCartDetailEntity.getProductEntity().getProductPrice())
                    .productQuantity(shopCartDetailEntity.getQuantity())
                    .productSizeNumber(shopCartDetailEntity.getSizeNumber())
                    .productImageUrl(imageRepository.findTop1ByProductProductIdAndColorColorName(shopCartDetailEntity.getProductEntity().getProductId(),
                            shopCartDetailEntity.getColor()).getImageUrl())
                    .productColor(shopCartDetailEntity.getColor())
                    .build();
            productCartResponses.add(productCartResponse);
        }
        return productCartResponses;

    }

    public ClientEntity getLoggedClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return clientRepository.findByUserEmail(authentication.getName());
    }

}
