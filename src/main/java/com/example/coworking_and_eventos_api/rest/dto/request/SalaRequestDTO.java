package com.example.coworking_and_eventos_api.rest.dto.request;

import com.example.coworking_and_eventos_api.enums.EnumTipoSala;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalaRequestDTO {

    private String nome;
    private Integer capacidade;
    private EnumTipoSala tipoSala;
    
}
