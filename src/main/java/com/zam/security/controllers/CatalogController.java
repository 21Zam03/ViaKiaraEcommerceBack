package com.zam.security.controllers;

import com.zam.security.payload.response.ImageResponse;
import com.zam.security.payload.response.ProductResponse;
import com.zam.security.payload.response.ProductSpecificResponse;
import com.zam.security.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CatalogController.API_PATH)
public class CatalogController {

    public final CatalogService catalogService;

    public static final String API_PATH = "/api/catalog";

    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping()
    public ResponseEntity<Page<ProductResponse>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "6") int size) {
        return new ResponseEntity<>(catalogService.getProducts(page, size), HttpStatus.OK);
    }

    @GetMapping("/{productId}/{colorName}")
    public ResponseEntity<ProductSpecificResponse> getProduct(@PathVariable Integer productId, @PathVariable String colorName) {
        return new ResponseEntity<>(catalogService.getProduct(productId, colorName), HttpStatus.OK);
    }

    @GetMapping("/colors")
    public ResponseEntity<List<ImageResponse>> getColors(@RequestParam Integer productId) {
        return new ResponseEntity<>(catalogService.getColorsImages(productId), HttpStatus.OK);
    }

}
