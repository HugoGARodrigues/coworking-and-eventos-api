package com.example.coworking_and_eventos_api.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import com.example.coworking_and_eventos_api.repository.ReservaRepository;
import com.example.coworking_and_eventos_api.rest.dto.response.SalaHorariosLivresResponseDTO;
import com.example.coworking_and_eventos_api.service.interfaces.ReservaService;
import com.example.coworking_and_eventos_api.service.interfaces.SalaService;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private AgendamentoValidador agendamentoValidador;

    @Autowired
    private SalaService salaService;
    

    @Override
    @Transactional
    public Reserva cadastrarReserva(Reserva reserva, Long salaId) throws Exception {
        Sala sala = salaService.buscarSalaPorId(salaId);
        reserva.setSala(sala);
        agendamentoValidador.validaTerminoAnteriorAoInicio(reserva.getDataInicioReserva().toLocalTime(), reserva.getDataFimReserva().toLocalTime());
        agendamentoValidador.validaTempoMinimo(reserva.getDataInicioReserva().toLocalTime(), reserva.getDataFimReserva().toLocalTime());
        agendamentoValidador.validaHorarioComercial(reserva.getDataInicioReserva().toLocalTime(), reserva.getDataFimReserva().toLocalTime());

        agendamentoValidador.validaConflitoReservas(reserva);
        
        reserva.setSala(sala);
        reserva.setStatusAgendamento(EnumStatusAgendamento.AGENDADO);
        return reservaRepository.save(reserva);
        
        
    }

    @Override
    public Reserva cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com o ID: " + id));

        reserva.setStatusAgendamento(EnumStatusAgendamento.CANCELADO);
        reservaRepository.save(reserva);
        return reserva;
    }

    
}
