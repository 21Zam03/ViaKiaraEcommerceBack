package com.zam.security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop_cart_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCartDetailEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "shop_cart_detail_id")
    private Integer shopCartDetailId;

    @ManyToOne
    @JoinColumn(name = "shop_cart_id")
    private ShopCartEntity shopCart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "size_number")
    private String sizeNumber;

    @Column(name = "color")
    private String color;

}
