package com.example.coworking_and_eventos_api.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SalaTest {

    @Test
    void deveTestarGettersESetters() {
        Sala sala = new Sala();
        
        List<Reserva> reservas = new ArrayList<>();
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reservas.add(reserva);

        sala.setId(1L);
        sala.setNome("Sala de Reunião Principal");
        sala.setCapacidade(10);
        sala.setTipoSala(null); // Usando null para contornar a ausência da classe EnumTipoSala no contexto atual
        sala.setReservas(reservas);

        assertEquals(1L, sala.getId());
        assertEquals("Sala de Reunião Principal", sala.getNome());
        assertEquals(10, sala.getCapacidade());
        assertNull(sala.getTipoSala());
        assertEquals(1, sala.getReservas().size());
        assertEquals(1L, sala.getReservas().get(0).getId());
    }

    @Test
    void deveTestarConstrutorComArgumentos() {
        Sala sala = new Sala(2L, "Auditório", 50, null);

        assertEquals(2L, sala.getId());
        assertEquals("Auditório", sala.getNome());
        assertEquals(50, sala.getCapacidade());
        assertNull(sala.getTipoSala());
        assertNull(sala.getReservas()); // Não é inicializado no construtor padrão da entidade
    }
}