package com.example.coworking_and_eventos_api.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumTipoSala;
import com.example.coworking_and_eventos_api.rest.dto.request.SalaRequestDTO;
import com.example.coworking_and_eventos_api.rest.dto.response.SalaResponseDTO;
import com.example.coworking_and_eventos_api.rest.factory.SalaRestFactory;

public class SalaRestFactoryTest {

    // Método auxiliar para obter um Enum simulado
    private EnumTipoSala getTipoSalaMock() {
        return EnumTipoSala.values().length > 0 ? EnumTipoSala.values()[0] : null;
    }

    @Test
    void deveConverterRequestDtoParaEntity() {
        SalaRequestDTO dto = new SalaRequestDTO();
        dto.setNome("Sala VIP");
        dto.setCapacidade(15);
        dto.setTipoSala(getTipoSalaMock());

        Sala sala = SalaRestFactory.getEntity(dto);

        assertNotNull(sala);
        assertEquals("Sala VIP", sala.getNome());
        assertEquals(15, sala.getCapacidade());
        assertEquals(getTipoSalaMock(), sala.getTipoSala());
    }

    @Test
    void deveConverterEntityParaResponseDto() {
        Sala sala = new Sala();
        sala.setId(1L);
        sala.setNome("Sala VIP");
        sala.setCapacidade(15);
        sala.setTipoSala(getTipoSalaMock());

        List<Reserva> reservas = new ArrayList<>();
        Reserva reserva = new Reserva();
        reserva.setId(10L);
        reservas.add(reserva);
        sala.setReservas(reservas);

        SalaResponseDTO dto = SalaRestFactory.getResponseDTO(sala);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Sala VIP", dto.getNome());
        assertEquals(15, dto.getCapacidade());
        assertEquals(getTipoSalaMock(), dto.getTipoSala());
        assertEquals(1, dto.getReservas().size());
        assertEquals(10L, dto.getReservas().get(0).getId());
    }

    @Test
    void deveConverterPageEntityParaPageDto() {
        Sala sala = new Sala();
        sala.setId(1L);
        sala.setNome("Auditório");

        Page<Sala> pageEntity = new PageImpl<>(List.of(sala), PageRequest.of(0, 10), 1);

        Page<SalaResponseDTO> pageDto = SalaRestFactory.fromPageEntityToPageDTO(pageEntity);

        assertNotNull(pageDto);
        assertEquals(1, pageDto.getTotalElements());
        assertEquals(1L, pageDto.getContent().get(0).getId());
        assertEquals("Auditório", pageDto.getContent().get(0).getNome());
    }
}