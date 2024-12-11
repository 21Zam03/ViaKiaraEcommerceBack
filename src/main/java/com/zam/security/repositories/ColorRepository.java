package com.zam.security.repositories;

import com.zam.security.entities.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, Integer> {

    public Optional<ColorEntity> findByColorName(String colorName);

}
