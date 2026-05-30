package com.example.coworking_and_eventos_api.rest.dto.response;

import java.time.LocalDateTime;

import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaResponseDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime dataInicioReserva;
    private LocalDateTime dataFimReserva;
    private EnumStatusAgendamento statusAgendamento;
    private Sala sala;
}
