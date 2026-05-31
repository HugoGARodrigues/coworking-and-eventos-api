package com.example.coworking_and_eventos_api.entity;

import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReservaTest {

    @Test
    void deveTestarGettersESetters() {
        Reserva reserva = new Reserva();
        Sala sala = new Sala();
        sala.setId(1L);

        LocalDateTime inicio = LocalDateTime.of(2023, 10, 10, 8, 0);
        LocalDateTime fim = LocalDateTime.of(2023, 10, 10, 9, 0);

        reserva.setId(10L);
        reserva.setDataInicioReserva(inicio);
        reserva.setDataFimReserva(fim);
        reserva.setStatusAgendamento(EnumStatusAgendamento.AGENDADO);
        reserva.setSala(sala);

        assertEquals(10L, reserva.getId());
        assertEquals(inicio, reserva.getDataInicioReserva());
        assertEquals(fim, reserva.getDataFimReserva());
        assertEquals(EnumStatusAgendamento.AGENDADO, reserva.getStatusAgendamento());
        assertEquals(sala, reserva.getSala());
    }

    @Test
    void deveTestarConstrutorComArgumentos() {
        Sala sala = new Sala();
        LocalDateTime inicio = LocalDateTime.of(2023, 10, 10, 8, 0);
        LocalDateTime fim = LocalDateTime.of(2023, 10, 10, 9, 0);

        Reserva reserva = new Reserva(1L, 200L, inicio, fim, sala);

        assertEquals(1L, reserva.getId());
        assertEquals(inicio, reserva.getDataInicioReserva());
        assertEquals(fim, reserva.getDataFimReserva());
        assertEquals(sala, reserva.getSala());
        assertNull(reserva.getStatusAgendamento()); // Não é setado no construtor padrão da entidade
    }
}