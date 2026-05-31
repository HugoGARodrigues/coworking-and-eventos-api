package com.example.coworking_and_eventos_api.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;

public interface ReservaService {

    public Reserva cadastrarReserva(Reserva reserva, Sala sala) throws Exception;

    public Reserva cancelarReserva(Long id);

    public List<Reserva> listarReservasPorSalaEDia(Long idSala, LocalDateTime dataInicio, LocalDateTime dataFim);


}
