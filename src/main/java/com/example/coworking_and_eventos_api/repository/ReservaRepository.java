package com.example.coworking_and_eventos_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.coworking_and_eventos_api.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {



    @Query("SELECT r FROM Reserva r WHERE r.sala.id = :salaId AND r.statusAgendamento = 'AGENDADO'")
    List<Reserva> findReservasBySalaIdAndStatusAgendado(@Param("salaId") Long salaId);

    @Query("SELECT r FROM Reserva r " +
    "WHERE r.sala.id = :salaId " +
    "AND r.statusAgendamento = 'AGENDADO' " +
    "AND r.dataInicioReserva >= :dataInicioComercial " +
    "AND r.dataFimReserva <= :dataFimComercial")
    List<Reserva> findReservasBySalaIdAndStatusAgendadoEDiaAgendado(@Param("salaId") Long salaId, 
                                                                    @Param("dataInicioComercial") LocalDateTime dataInicioComercial, 
                                                                    @Param("dataFimComercial") LocalDateTime dataFimComercial);

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



}
