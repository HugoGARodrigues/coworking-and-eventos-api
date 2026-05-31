package com.example.coworking_and_eventos_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.repository.SalaRepository;
import com.example.coworking_and_eventos_api.service.interfaces.ReservaService;

@ExtendWith(MockitoExtension.class)
public class SalaServiceImplTest {

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private SalaServiceImpl salaService;

    @Test
    void deveCriarSalaComSucesso() {
        Sala sala = new Sala(null, "Auditório", 50, null);
        Sala salaSalva = new Sala(1L, "Auditório", 50, null);
        
        when(salaRepository.save(any(Sala.class))).thenReturn(salaSalva);

        Sala resultado = salaService.criarSala(sala);

        assertEquals(1L, resultado.getId());
        assertEquals("Auditório", resultado.getNome());
        verify(salaRepository).save(sala);
    }

    @Test
    void deveLancarExcecaoSeSalaNaoEncontrada() {
        when(salaRepository.findById(99L)).thenReturn(Optional.empty());
        
        Exception ex = assertThrows(Exception.class, () -> salaService.buscarSalaPorId(99L));
        assertEquals("Sala não encontrada com o ID: 99", ex.getMessage());
    }

    @Test
    void deveValidarConflitoDeSlotsCorretamente() {
        Reserva reservaExistente = new Reserva();
        reservaExistente.setDataInicioReserva(LocalDateTime.of(2023, 10, 10, 8, 0));
        reservaExistente.setDataFimReserva(LocalDateTime.of(2023, 10, 10, 9, 0));

        List<Reserva> reservas = List.of(reservaExistente);

        assertTrue(salaService.isSlotConflitante("08:00 - 09:00", reservas));
        
        assertFalse(salaService.isSlotConflitante("09:00 - 10:00", reservas));
        assertFalse(salaService.isSlotConflitante("10:00 - 11:00", reservas));
    }

    @Test
    void deveValidarNomeDaSalaIgual() {
        Sala salaExistente = new Sala(1L, "Sala Principal", 20, null);
        when(salaRepository.findByNome("Sala Principal")).thenReturn(salaExistente);

        Sala novaSala = new Sala(null, "Sala Principal", 30, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
            () -> salaService.nomeSalaIgualValidador(novaSala));
            
        assertEquals("Já existe uma sala com o nome: Sala Principal", ex.getMessage());
    }
}