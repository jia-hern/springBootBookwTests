package com.myorg.booklibrary.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.myorg.booklibrary.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
