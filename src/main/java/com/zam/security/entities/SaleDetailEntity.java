package com.zam.security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sale_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_detail_id")
    private Integer saleDetailId;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private SaleEntity saleEntity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "color")
    private String colorName;

    @Column(name = "size_number")
    private String sizeNumber;

}
