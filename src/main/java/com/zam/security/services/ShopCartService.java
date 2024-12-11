package com.zam.security.services;

import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.response.ProductCartResponse;

import java.util.List;

public interface ShopCartService {

    public MessageResponse addProductToCart(Integer productId, Integer quantity, String sizeNumber, String color);
    public MessageResponse removeProductFromCart(Integer productId);
    public MessageResponse addDiscountToCart(String discountCode);
    public List<ProductCartResponse> getProductsInCart();

}
