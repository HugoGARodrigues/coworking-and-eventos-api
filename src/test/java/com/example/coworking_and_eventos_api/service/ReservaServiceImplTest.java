package com.example.coworking_and_eventos_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import com.example.coworking_and_eventos_api.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private AgendamentoValidador agendamentoValidador;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Test
    void deveCadastrarReservaComSucesso() throws Exception {
        Sala sala = new Sala();
        sala.setId(1L);
        
        Reserva reserva = new Reserva();
        reserva.setDataInicioReserva(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0));
        reserva.setDataFimReserva(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));
        
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> {
            Reserva r = i.getArgument(0);
            r.setId(10L);
            return r;
        });

        Reserva salva = reservaService.cadastrarReserva(reserva, sala);

        assertNotNull(salva.getId());
        assertEquals(EnumStatusAgendamento.AGENDADO, salva.getStatusAgendamento());
        assertEquals(sala, salva.getSala());
        verify(agendamentoValidador).validaConflitoReservas(any());
    }

    @Test
    void deveCancelarReservaComSucesso() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setStatusAgendamento(EnumStatusAgendamento.AGENDADO);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva cancelada = reservaService.cancelarReserva(1L);

        assertEquals(EnumStatusAgendamento.CANCELADO, cancelada.getStatusAgendamento());
        verify(reservaRepository).save(reserva);
    }

    @Test
    void deveLancarExcecaoAoEditarReservaCancelada() {
        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(1L);
        reservaExistente.setStatusAgendamento(EnumStatusAgendamento.CANCELADO);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaExistente));

        Reserva reservaAtualizada = new Reserva();
        reservaAtualizada.setDataInicioReserva(LocalDateTime.now().plusDays(1));
        reservaAtualizada.setDataFimReserva(LocalDateTime.now().plusDays(1).plusHours(1));

        IllegalStateException ex = assertThrows(IllegalStateException.class, 
            () -> reservaService.editarReserva(1L, reservaAtualizada));
            
        assertEquals("Não é possível editar uma reserva cancelada.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCancelarReservaNaoExistente() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> reservaService.cancelarReserva(99L));
    }
}