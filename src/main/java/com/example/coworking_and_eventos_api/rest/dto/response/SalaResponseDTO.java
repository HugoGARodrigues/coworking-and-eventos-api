package com.example.coworking_and_eventos_api.rest.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.enums.EnumTipoSala;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalaResponseDTO {

    private Long id;
    private String nome;
    private Integer capacidade;
    private List<Reserva> reservas;
    private EnumTipoSala tipoSala;

}
    

