package com.example.coworking_and_eventos_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.coworking_and_eventos_api.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findReservasBySalaIdAndStatusAgendado(Long salaId);

}
