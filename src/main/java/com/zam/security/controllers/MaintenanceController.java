package com.zam.security.controllers;

import com.zam.security.payload.request.ProductImagesRequest;
import com.zam.security.payload.request.StockRequest;
import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.request.ProductCreateRequest;
import com.zam.security.services.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(MaintenanceController.API_PATH)
public class MaintenanceController {

    public static final String API_PATH = "/api/maintenance";
    public static final String PRODUCT_PATH = "/products";

    private final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @PostMapping(value = MaintenanceController.PRODUCT_PATH)
    public ResponseEntity<MessageResponse> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        return new ResponseEntity<>(maintenanceService.createProduct(productCreateRequest), HttpStatus.CREATED);
    }

    @PostMapping(value = MaintenanceController.PRODUCT_PATH+"/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> addImagesToProduct(
            @RequestParam("productId") String productId,
            @RequestParam("fileList") List<MultipartFile> fileList,
            @RequestParam("colorList") List<String> colorList) throws Exception {
        ProductImagesRequest productImagesRequest = ProductImagesRequest.builder()
                .productId(Integer.parseInt(productId))
                .fileList(fileList)
                .colorList(colorList)
                .build();
        return new ResponseEntity<>(maintenanceService.addImagesToProduct(productImagesRequest), HttpStatus.CREATED);
    }

    @PostMapping(value = MaintenanceController.PRODUCT_PATH+"/stock")
    public ResponseEntity<MessageResponse> addStockToProduct(@RequestBody StockRequest stockRequest) {
        return new ResponseEntity<>(maintenanceService.addStockToProduct(stockRequest), HttpStatus.CREATED);
    }

}
