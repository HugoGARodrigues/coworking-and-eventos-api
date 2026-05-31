package com.example.coworking_and_eventos_api.rest.factory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import com.example.coworking_and_eventos_api.rest.dto.request.SalaRequestDTO;
import com.example.coworking_and_eventos_api.rest.dto.response.SalaResponseDTO;


public class SalaRestFactory {
    public static Sala getEntity(SalaRequestDTO dto){
        Sala sala = new Sala();
        sala.setNome(dto.getNome());
        sala.setCapacidade(dto.getCapacidade());
        sala.setTipoSala(dto.getTipoSala());
        return sala;
    }

    public static SalaResponseDTO getResponseDTO(Sala sala){
        SalaResponseDTO dto = new SalaResponseDTO();
        dto.setId(sala.getId());
        dto.setNome(sala.getNome());
        dto.setCapacidade(sala.getCapacidade());
        dto.setTipoSala(sala.getTipoSala());
        
        if (sala.getReservas() != null) {
            List<Reserva> reservasAgendadas = sala.getReservas().stream()
                    .filter(reserva -> reserva.getStatusAgendamento() == EnumStatusAgendamento.AGENDADO)
                    .collect(Collectors.toList());
            dto.setReservas(reservasAgendadas);
        }
        
        return dto;
    }

    public static Page<SalaResponseDTO> fromPageEntityToPageDTO(Page<Sala> salas){
        List<SalaResponseDTO> dtos = salas.map(sala -> getResponseDTO(sala)).getContent();
        return new PageImpl<>(dtos, salas.getPageable(), salas.getTotalElements());
    }

}
