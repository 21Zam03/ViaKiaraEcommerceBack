package com.zam.security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "sale")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Integer saleId;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "total")
    private Double total;

}

