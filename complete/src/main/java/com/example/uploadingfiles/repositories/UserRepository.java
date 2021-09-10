package com.example.uploadingfiles.repositories;

import com.example.uploadingfiles.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    long countUsersByUsername(String username);

    User findByUsername(String username);
}
