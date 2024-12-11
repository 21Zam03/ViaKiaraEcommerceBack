package com.zam.security.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCreateRequest {

    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productCategory;
    private boolean productOffer;
    private Integer productDiscount;
    private Integer productStock;

}
