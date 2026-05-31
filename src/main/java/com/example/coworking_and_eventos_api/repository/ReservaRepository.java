package com.example.coworking_and_eventos_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r " +
           "WHERE r.sala.id = :salaId " +
           "AND r.statusAgendamento = 'AGENDADO' " +
           "AND r.dataInicioReserva >= :inicio " +
           "AND r.dataInicioReserva <= :fim " +
           "ORDER BY r.dataInicioReserva ASC")
    List<Reserva> buscarReservasPorIntervaloAndStatusAgendado(
            @Param("salaId") Long salaId, 
            @Param("inicio") LocalDateTime inicio, 
            @Param("fim") LocalDateTime fim);

       
       @Query("SELECT r FROM Reserva r " +
           "WHERE r.sala.id = :salaId " +
           "AND r.statusAgendamento = :status " +
           "ORDER BY r.sala.id ASC")
       Page<Reserva> buscarReservasPorIntervaloAndStatus(
            @Param("salaId") Long salaId, 
            @Param("status") EnumStatusAgendamento status,
            Pageable pageable);
       


}
