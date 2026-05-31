package com.example.coworking_and_eventos_api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
public class AgendamentoValidadorTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private AgendamentoValidador validador;

    @Test
    void devePassarQuandoTerminoForAposOInicio() {
        assertDoesNotThrow(() -> validador.validaTerminoAnteriorAoInicio(LocalTime.of(8, 0), LocalTime.of(9, 0)));
    }

    @Test
    void deveLancarExcecaoQuandoTerminoForAnteriorAoInicio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
            () -> validador.validaTerminoAnteriorAoInicio(LocalTime.of(9, 0), LocalTime.of(8, 0)));
        assertEquals("O horário de término não pode ser anterior ao início.", ex.getMessage());
    }

    @Test
    void devePassarQuandoAluguelForEmHorarioComercial() {
        assertDoesNotThrow(() -> validador.validaHorarioComercial(LocalTime.of(8, 0), LocalTime.of(18, 0)));
    }

    @Test
    void deveLancarExcecaoQuandoAluguelForForaDoHorarioComercial() {
        assertThrows(IllegalArgumentException.class, () -> validador.validaHorarioComercial(LocalTime.of(7, 0), LocalTime.of(9, 0)));
        assertThrows(IllegalArgumentException.class, () -> validador.validaHorarioComercial(LocalTime.of(17, 0), LocalTime.of(19, 0)));
    }

    @Test
    void deveLancarExcecaoQuandoDuracaoForMenorQueUmaHora() {
        assertThrows(IllegalArgumentException.class, () -> validador.validaTempoMinimo(LocalTime.of(8, 0), LocalTime.of(8, 30)));
    }

    @Test
    void deveLancarExcecaoQuandoAluguelForNoPassado() {
        LocalDateTime passado = LocalDateTime.now().minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> validador.validaAluguelNoPassado(passado));
    }

    @Test
    void devePassarQuandoAluguelNoFuturo() {
        LocalDateTime futuro = LocalDateTime.now().plusDays(1);
        assertDoesNotThrow(() -> validador.validaAluguelNoPassado(futuro));
    }

    @Test
    void deveLancarExcecaoParaSlotInvalido() {
        assertThrows(IllegalArgumentException.class, () -> validador.validaAluguelNoPadraoDosSlots(LocalTime.of(8, 30), LocalTime.of(9, 30)));
    }

    @Test
    void deveLancarExcecaoQuandoHouverConflitoDeReservas() {
        Sala sala = new Sala();
        sala.setId(1L);
        Reserva novaReserva = new Reserva(null, 1L, LocalDateTime.of(2030, 10, 10, 8, 0), LocalDateTime.of(2030, 10, 10, 9, 0), sala);
        
        Reserva reservaExistente = new Reserva(null, 2L, LocalDateTime.of(2030, 10, 10, 8, 30), LocalDateTime.of(2030, 10, 10, 9, 30), sala);
        
        when(reservaRepository.buscarReservasPorIntervaloAndStatusAgendado(eq(1L), any(), any()))
            .thenReturn(List.of(reservaExistente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validador.validaConflitoReservas(novaReserva));
        assertEquals("A sala já está reservada para o período solicitado.", ex.getMessage());
    }
}