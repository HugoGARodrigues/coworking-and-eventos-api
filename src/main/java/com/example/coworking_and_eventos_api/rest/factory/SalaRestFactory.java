package com.example.coworking_and_eventos_api.rest.factory;

import com.example.coworking_and_eventos_api.entity.Sala;
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
        return dto;
    }

}
