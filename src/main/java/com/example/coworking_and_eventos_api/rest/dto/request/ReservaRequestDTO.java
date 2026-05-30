package com.example.coworking_and_eventos_api.rest.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRequestDTO {
    private Long clienteId;
    private LocalDateTime dataInicioReserva; // Formato: "yyyy-MM-dd'T'HH:mm"
    private LocalDateTime dataFimReserva; // Formato: "yyyy-MM-dd'T'HH:mm"

}
