package com.zam.security.repositories;

import com.zam.security.entities.ProductEntity;
import com.zam.security.entities.ShopCartDetailEntity;
import com.zam.security.entities.ShopCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopCartDetailRepository extends JpaRepository<ShopCartDetailEntity, Integer> {

    public boolean existsByShopCartAndProductEntity(ShopCartEntity shopCart, ProductEntity productEntity);
    public void deleteByShopCartAndProductEntity(ShopCartEntity shopCart, ProductEntity productEntity);
    public List<ShopCartDetailEntity> findByShopCart(ShopCartEntity shopCart);
    public void deleteByShopCart(ShopCartEntity shopCart);
}
