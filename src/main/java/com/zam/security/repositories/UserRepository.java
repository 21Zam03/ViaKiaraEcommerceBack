package com.zam.security.repositories;

import com.zam.security.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    public Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

}
