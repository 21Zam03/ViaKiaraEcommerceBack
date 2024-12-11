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
public class ProductSpecificResponse {

    private Integer productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private List<String> productImageUrl;
    private String productCategory;
    private Integer productStock;
    private Boolean productOffer;
    private Integer productDiscount;
    private List<String> sizeList;

}
