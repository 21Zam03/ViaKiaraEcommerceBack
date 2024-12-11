package com.zam.security.repositories;

import com.zam.security.entities.ClientEntity;
import com.zam.security.entities.ShopCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopCartRepository extends JpaRepository<ShopCartEntity, Integer> {

    public Optional<ShopCartEntity> findByClient(ClientEntity client);

}
