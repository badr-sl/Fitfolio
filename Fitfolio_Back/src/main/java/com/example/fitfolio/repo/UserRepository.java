package com.example.fitfolio.repo;

import com.example.fitfolio.entities.Utilisateur;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByUsername(String username);
    Optional<Utilisateur> findById(Long id);}