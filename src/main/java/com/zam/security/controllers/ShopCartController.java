package com.zam.security.controllers;

import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.response.ProductCartResponse;
import com.zam.security.services.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ShopCartController.API_PATH)
public class ShopCartController {

    public static final String API_PATH = "/api/shopcart";

    public final ShopCartService shopCartService;

    @Autowired
    public ShopCartController(ShopCartService shopCartService) {
        this.shopCartService = shopCartService;
    }

    @GetMapping
    public ResponseEntity<List<ProductCartResponse>> getProductsInCart(){
        return new ResponseEntity<>(shopCartService.getProductsInCart(), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addProductToCart(
            @RequestParam Integer productId, @RequestParam int quantity, @RequestParam String sizeNumber,
            @RequestParam String color) {
        return new ResponseEntity<>(shopCartService.addProductToCart(productId, quantity, sizeNumber, color), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteProductFromCart(@RequestParam Integer productId) {
        return new ResponseEntity<>(shopCartService.removeProductFromCart(productId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<MessageResponse> addDiscountToCart(@RequestParam String discountCode) {
        return new ResponseEntity<>(shopCartService.addDiscountToCart(discountCode), HttpStatus.OK);
    }

}
