package com.zam.security.repositories;

import com.zam.security.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    public boolean existsByProductName(String productName);
    public boolean existsByProductId(Integer productId);

}
