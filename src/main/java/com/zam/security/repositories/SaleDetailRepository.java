package com.zam.security.repositories;

import com.zam.security.entities.SaleDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetailEntity, Integer> {

    public List<SaleDetailEntity> findBySaleEntitySaleId(Integer saleId);

}
