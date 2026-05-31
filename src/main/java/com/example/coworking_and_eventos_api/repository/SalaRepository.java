package com.example.coworking_and_eventos_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumTipoSala;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    public List<Sala> findByTipoSala(EnumTipoSala tipoSala);

    @Query("SELECT DISTINCT s FROM Sala s " +
           "JOIN s.reservas r " +
           "WHERE r.dataInicioReserva >= :dataInicio " +
           "AND r.dataInicioReserva <= :dataFim " +
           "AND r.statusAgendamento = 'AGENDADO'"
           )
    public Page<Sala> listarPaginadoComFiltroDeDiaDaReserva(@Param("dataInicio") LocalDateTime dataInicio, 
                                                            @Param("dataFim") LocalDateTime dataFim,
                                                            Pageable pageable);

    
    
}
