package com.example.coworking_and_eventos_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.coworking_and_eventos_api.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.sala.id = :salaId AND r.statusAgendamento = 'AGENDADO'")
    List<Reserva> findReservasBySalaIdAndStatusAgendado(@Param("salaId") Long salaId);

}
