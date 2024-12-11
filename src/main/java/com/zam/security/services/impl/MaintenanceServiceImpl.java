package com.zam.security.services.impl;

import com.zam.security.entities.*;
import com.zam.security.exceptions.NotFoundException;
import com.zam.security.payload.request.InfoColorRequest;
import com.zam.security.payload.request.ProductImagesRequest;
import com.zam.security.payload.request.StockRequest;
import com.zam.security.payload.response.FileResponse;
import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.request.ProductCreateRequest;
import com.zam.security.repositories.*;
import com.zam.security.services.FireBaseStorageService;
import com.zam.security.services.MaintenanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private final ProductRepository productRepository;
    private final FireBaseStorageService fireBaseStorageService;
    private final ImageRepository imageRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final StockRepository stockRepository;

    public MaintenanceServiceImpl(ProductRepository productRepository, FireBaseStorageService fireBaseStorageService,
                                  ImageRepository imageRepository, SizeRepository sizeRepository,
                                  ColorRepository colorRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.fireBaseStorageService = fireBaseStorageService;
        this.imageRepository = imageRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public MessageResponse createProduct(ProductCreateRequest productCreateRequest) {
        if (productRepository.existsByProductName(productCreateRequest.getProductName())) {
            return new MessageResponse("Product already exists in the system");
        }
        ProductEntity productEntity = ProductEntity.builder()
                .productName(productCreateRequest.getProductName())
                .productDescription(productCreateRequest.getProductDescription())
                .productPrice(productCreateRequest.getProductPrice())
                .productCategory(productCreateRequest.getProductCategory())
                .productOffer(productCreateRequest.isProductOffer())
                .productDiscount(productCreateRequest.getProductDiscount())
                .productStock(productCreateRequest.getProductStock())
                .build();
        productRepository.save(productEntity);
        return new MessageResponse("Product created successfully");
    }

    @Override
    public MessageResponse addImagesToProduct(ProductImagesRequest productImagesRequest) throws Exception {
        ProductEntity productEntity = productRepository.findById(productImagesRequest.getProductId()).orElseThrow(() -> {
            return new NotFoundException("Product not found");
        });

        List<FileResponse> fileResponseList = fireBaseStorageService.uploadFile(productImagesRequest.getFileList());

        for (int i=0; i<fileResponseList.size(); i++) {
            ColorEntity color = colorRepository.findByColorName(productImagesRequest.getColorList().get(i)).orElseThrow(() -> {
                return new NotFoundException("Color not found");
            });
            ImageEntity imageToCreate = ImageEntity.builder()
                    .product(productEntity)
                    .color(color)
                    .imageUrl(fileResponseList.get(i).getUrl())
                    .imageDescription("")
                    .build();
            imageRepository.save(imageToCreate);
        }
        return new MessageResponse("Successfully added image to product");
    }

    @Override
    public MessageResponse addStockToProduct(StockRequest stockRequest) {
        ProductEntity productEntity = productRepository.findById(stockRequest.getProductId()).orElseThrow(() -> {
            return new NotFoundException("Product not found");
        });

        for (int i = 0; i<stockRequest.getInfoColorRequestList().size(); i++) {
            ColorEntity colorEntity = colorRepository.findByColorName(stockRequest.getInfoColorRequestList().get(i).getColorName()).orElseThrow(() -> {
                return new NotFoundException("Color not found");
            });

            for (int j = 0; j<stockRequest.getInfoColorRequestList().get(i).getSizeRequestList().size(); j++) {
                SizeEntity sizeEntity = sizeRepository.findBySizeNumber(stockRequest.getInfoColorRequestList().get(i).getSizeRequestList().get(j).getSizeNumber()).orElseThrow(() -> {
                    return new NotFoundException("Size not found");
                });
                StockEntity stockEntity = StockEntity.builder()
                        .product(productEntity)
                        .color(colorEntity)
                        .size(sizeEntity)
                        .stockQuantity(stockRequest.getInfoColorRequestList().get(i).getSizeRequestList().get(j).getStockQuantity())
                        .build();
                stockRepository.save(stockEntity);
            }
        }
        return new MessageResponse("Successfully added stock to product");
    }

}
