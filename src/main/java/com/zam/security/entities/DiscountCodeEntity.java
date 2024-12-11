package com.zam.security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "discount_code")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountCodeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "discount_code_id")
    private Integer discountCodeId;

    @Column(name = "discount_code")
    private String discountCode;

    @Column(name = "discount_value")
    private Integer discountValue;

}
