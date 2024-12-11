package com.zam.security.repositories;

import com.zam.security.entities.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Integer> {

    @Query("SELECT DISTINCT s.size.sizeNumber FROM StockEntity s WHERE s.product.productId = :productId")
    public List<String> findDistinctTallasByProducto(Integer productId);

}
