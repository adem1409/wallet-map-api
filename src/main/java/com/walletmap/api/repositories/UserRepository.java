package com.walletmap.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.walletmap.api.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}