package com.example.coworking_and_eventos_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.coworking_and_eventos_api.entity.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    
}
