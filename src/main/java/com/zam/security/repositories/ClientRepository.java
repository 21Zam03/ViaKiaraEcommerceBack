package com.zam.security.repositories;

import com.zam.security.entities.ClientEntity;
import com.zam.security.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {

    public ClientEntity findByUser(UserEntity user);
    public ClientEntity findByUserEmail(String email);

}
