package com.zam.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Integer productId;
    private String productName;
    private Double productPrice;
    private List<ImageResponse> imageResponseList;
    private Boolean productOffer;
    private Integer productDiscount;

}
