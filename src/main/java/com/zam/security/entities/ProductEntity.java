package com.zam.security.entities;

import ch.qos.logback.core.model.NamedModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String productDescription;

    @Column(name = "product_price")
    private Double productPrice;

    @Column(name = "category")
    private String productCategory;

    @Column(name = "offer")
    private Boolean productOffer;

    @Column(name = "discount")
    private Integer productDiscount;

    @Column(name = "stock")
    private Integer productStock;

}
