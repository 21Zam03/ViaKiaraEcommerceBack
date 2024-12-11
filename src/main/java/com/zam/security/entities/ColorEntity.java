package com.zam.security.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "color")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id")
    private Integer colorId;

    @Column(name = "color_name", nullable = false)
    private String colorName;

}
