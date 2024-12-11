package com.zam.security.services;

import com.zam.security.payload.request.InfoColorRequest;
import com.zam.security.payload.request.ProductImagesRequest;
import com.zam.security.payload.request.StockRequest;
import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.request.ProductCreateRequest;

import java.util.List;

public interface MaintenanceService {

    public MessageResponse createProduct(ProductCreateRequest productCreateRequest);
    public MessageResponse addImagesToProduct(ProductImagesRequest productImagesRequest) throws Exception;
    public MessageResponse addStockToProduct(StockRequest stockRequest);

}
