package com.example.coworking_and_eventos_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumTipoSala;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    public List<Sala> findByTipoSala(EnumTipoSala tipoSala);
    
}
