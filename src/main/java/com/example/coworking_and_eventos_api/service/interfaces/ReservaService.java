package com.example.coworking_and_eventos_api.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.rest.dto.response.SalaHorariosLivresResponseDTO;

public interface ReservaService {

    public Reserva cadastrarReserva(Reserva reserva, Long salaId) throws Exception;

    public Reserva cancelarReserva(Long id);


}
