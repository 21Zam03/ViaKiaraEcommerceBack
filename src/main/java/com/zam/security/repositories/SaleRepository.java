package com.zam.security.repositories;

import com.zam.security.entities.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Integer> {

    public List<SaleEntity> findByClientClientId(Integer clientId);

}
