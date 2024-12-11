package com.zam.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCartResponse {

    private Integer productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private Integer productQuantity;
    private String productImageUrl;
    private String productSizeNumber;
    private String productColor;
}
