package com.zam.security.repositories;

import com.zam.security.entities.SizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<SizeEntity, Integer> {

    public Optional<SizeEntity> findBySizeNumber(String sizeNumber);

}
