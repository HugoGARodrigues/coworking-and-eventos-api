package com.example.coworking_and_eventos_api.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;

public interface ReservaService {

    public Reserva cadastrarReserva(Reserva reserva, Sala sala) throws Exception;

    public Reserva cancelarReserva(Long id);

    public List<Reserva> listarReservasPorSalaEDia(Long idSala, LocalDateTime dataInicio, LocalDateTime dataFim);
    
    public Page<Reserva> listarReservasPorSalaEDiaPaginada(Long idSala, EnumStatusAgendamento status, Integer paginaAtual, Integer tamanhoPagina, String direcao,
                                         String ordenacao);

    public Reserva editarReserva(Long idReserva, Reserva reservaAtualizada) throws Exception;


}
