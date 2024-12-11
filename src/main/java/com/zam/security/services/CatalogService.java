package com.zam.security.services;

import com.zam.security.payload.response.ImageResponse;
import com.zam.security.payload.response.ProductResponse;
import com.zam.security.payload.response.ProductSpecificResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CatalogService {

    public Page<ProductResponse> getProducts(int page, int size);
    public ProductSpecificResponse getProduct(Integer productId, String colorName);
    public List<ImageResponse> getColorsImages(Integer productId);

}
