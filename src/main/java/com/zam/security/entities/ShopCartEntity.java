package com.zam.security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "shop_cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCartEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer shopCartId;

    @OneToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "discount")
    private Integer discount;

}
