package com.zam.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetailResponse {

    private Integer saleDetailId;
    private String productName;
    private Integer quantity;
    private Double price;
    private String productImage;
    private String colorName;
    private String sizeNumber;

}
