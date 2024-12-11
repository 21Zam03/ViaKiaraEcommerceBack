package com.zam.security.services.impl;

import com.zam.security.entities.ColorEntity;
import com.zam.security.entities.ImageEntity;
import com.zam.security.entities.ProductEntity;
import com.zam.security.exceptions.NotFoundException;
import com.zam.security.payload.response.ImageResponse;
import com.zam.security.payload.response.ProductResponse;
import com.zam.security.payload.response.ProductSpecificResponse;
import com.zam.security.repositories.*;
import com.zam.security.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final ColorRepository colorRepository;
    private final StockRepository stockRepository;
    private final SizeRepository sizeRepository;

    @Autowired
    public CatalogServiceImpl(ProductRepository productRepository, ImageRepository imageRepository,
                              ColorRepository colorRepository, StockRepository stockRepository, SizeRepository sizeRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.colorRepository = colorRepository;
        this.stockRepository = stockRepository;
        this.sizeRepository = sizeRepository;
    }

    @Override
    public Page<ProductResponse> getProducts(int page, int size) {
        Sort.Direction direction = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "productId"));
        Page<ProductEntity> products = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = new ArrayList<>();

        for (int i=0; i<products.getContent().size(); i++) {
            List<Object[]> results = imageRepository.findImagesGroupedByColor(products.getContent().get(i).getProductId());
            List<ImageResponse> imageList = new ArrayList<>();

            for (Object[] result : results) {
                ImageResponse imageResponse = ImageResponse.builder()
                        .imageUrl((String) result[3])
                        .color(colorRepository.findById((Integer) result[2]).orElseThrow(() -> {
                            return new NotFoundException("Image not found");
                        }).getColorName())
                        .build();
                imageList.add(imageResponse);
            }

            ProductResponse productResponse = ProductResponse.builder()
                    .productId(products.getContent().get(i).getProductId())
                    .productName(products.getContent().get(i).getProductName())
                    .productPrice(products.getContent().get(i).getProductPrice())
                    .imageResponseList(imageList)
                    .productOffer(products.getContent().get(i).getProductOffer())
                    .productDiscount(products.getContent().get(i).getProductDiscount())
                    .build();
            productResponses.add(productResponse);
        }
        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    @Override
    public ProductSpecificResponse getProduct(Integer productId, String colorName) {

        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> {
            return new NotFoundException("Product with id " + productId + " not found");
        });

        ColorEntity colorEntity = colorRepository.findByColorName(colorName).orElseThrow(() -> {
            return new NotFoundException("Color with name " + colorName + " not found");
        });

        List<ImageEntity> imageList = imageRepository.findByProductProductIdAndColorColorId
                (productEntity.getProductId(), colorEntity.getColorId());
        List<String> imageUrlList = new ArrayList<>();
        for (ImageEntity image : imageList) {
            imageUrlList.add(image.getImageUrl());
        }
        List<String> sizeList = stockRepository.findDistinctTallasByProducto(productEntity.getProductId());
        return ProductSpecificResponse.builder()
                .productId(productEntity.getProductId())
                .productName(productEntity.getProductName())
                .productCategory(productEntity.getProductCategory())
                .productDescription(productEntity.getProductDescription())
                .productStock(productEntity.getProductStock())
                .productDiscount(productEntity.getProductDiscount())
                .productOffer(productEntity.getProductOffer())
                .productPrice(productEntity.getProductPrice())
                .productImageUrl(imageUrlList)
                .sizeList(sizeList)
                .build();
    }

    @Override
    public List<ImageResponse> getColorsImages(Integer productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> {
            return new NotFoundException("Product with id " + productId + " not found");
        });

        List<Object[]> results = imageRepository.findImagesGroupedByColor(product.getProductId());
        List<ImageResponse> imageList = new ArrayList<>();

        for (Object[] result : results) {
            ImageResponse imageResponse = ImageResponse.builder()
                    .imageUrl((String) result[3])
                    .color(colorRepository.findById((Integer) result[2]).orElseThrow(() -> {
                        return new NotFoundException("Image not found");
                    }).getColorName())
                    .build();
            imageList.add(imageResponse);
        }

        return imageList;
    }

}
