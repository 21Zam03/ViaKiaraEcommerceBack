package com.zam.security.repositories;

import com.zam.security.entities.DiscountCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountCodeRepository extends JpaRepository<DiscountCodeEntity, Integer> {

    public Optional<DiscountCodeEntity> findByDiscountCode(String discountCode);

}
