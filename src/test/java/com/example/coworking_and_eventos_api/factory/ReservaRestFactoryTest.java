package com.example.coworking_and_eventos_api.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import com.example.coworking_and_eventos_api.rest.dto.request.ReservaRequestDTO;
import com.example.coworking_and_eventos_api.rest.dto.response.ReservaResponseDTO;
import com.example.coworking_and_eventos_api.rest.factory.ReservaRestFactory;

public class ReservaRestFactoryTest {

    @Test
    void deveConverterRequestDtoParaEntity() {
        ReservaRequestDTO dto = new ReservaRequestDTO();
        LocalDateTime inicio = LocalDateTime.of(2023, 10, 10, 8, 0);
        LocalDateTime fim = LocalDateTime.of(2023, 10, 10, 10, 0);
        dto.setDataInicioReserva(inicio);
        dto.setDataFimReserva(fim);

        Reserva reserva = ReservaRestFactory.getEntity(dto);

        assertNotNull(reserva);
        assertEquals(inicio, reserva.getDataInicioReserva());
        assertEquals(fim, reserva.getDataFimReserva());
    }

    @Test
    void deveConverterEntityParaResponseDto() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        LocalDateTime inicio = LocalDateTime.of(2023, 10, 10, 8, 0);
        LocalDateTime fim = LocalDateTime.of(2023, 10, 10, 10, 0);
        reserva.setDataInicioReserva(inicio);
        reserva.setDataFimReserva(fim);
        reserva.setStatusAgendamento(EnumStatusAgendamento.AGENDADO);
        
        Sala sala = new Sala();
        sala.setId(2L);
        reserva.setSala(sala);

        ReservaResponseDTO dto = ReservaRestFactory.getResponseDTO(reserva);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(inicio, dto.getDataInicioReserva());
        assertEquals(fim, dto.getDataFimReserva());
        assertEquals(EnumStatusAgendamento.AGENDADO, dto.getStatusAgendamento());
        assertEquals(2L, dto.getSala().getId());
    }

    @Test
    void deveConverterPageEntityParaPageDto() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setStatusAgendamento(EnumStatusAgendamento.AGENDADO);

        Page<Reserva> pageEntity = new PageImpl<>(List.of(reserva), PageRequest.of(0, 10), 1);

        Page<ReservaResponseDTO> pageDto = ReservaRestFactory.fromPageEntityToPageDTO(pageEntity);

        assertNotNull(pageDto);
        assertEquals(1, pageDto.getTotalElements());
        assertEquals(1L, pageDto.getContent().get(0).getId());
        assertEquals(EnumStatusAgendamento.AGENDADO, pageDto.getContent().get(0).getStatusAgendamento());
    }
}