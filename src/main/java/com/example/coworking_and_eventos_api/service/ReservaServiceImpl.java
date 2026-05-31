package com.example.coworking_and_eventos_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import com.example.coworking_and_eventos_api.repository.ReservaRepository;
import com.example.coworking_and_eventos_api.service.interfaces.ReservaService;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;

    private final AgendamentoValidador agendamentoValidador;

    public ReservaServiceImpl(ReservaRepository reservaRepository, AgendamentoValidador agendamentoValidador) {
        this.reservaRepository = reservaRepository;
        this.agendamentoValidador = agendamentoValidador;
    }

    @Override
    @Transactional
    public Reserva cadastrarReserva(Reserva reserva, Sala sala) throws Exception {

        reserva.setSala(sala);
        agendamentoValidador.validaAluguelNoPadraoDosSlots(reserva.getDataInicioReserva().toLocalTime(), reserva.getDataFimReserva().toLocalTime());
        agendamentoValidador.validaTerminoAnteriorAoInicio(reserva.getDataInicioReserva().toLocalTime(), reserva.getDataFimReserva().toLocalTime());
        agendamentoValidador.validaTempoMinimo(reserva.getDataInicioReserva().toLocalTime(), reserva.getDataFimReserva().toLocalTime());
        agendamentoValidador.validaHorarioComercial(reserva.getDataInicioReserva().toLocalTime(), reserva.getDataFimReserva().toLocalTime());

        agendamentoValidador.validaConflitoReservas(reserva);
        agendamentoValidador.validaAluguelNoPassado(reserva.getDataInicioReserva());

        
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

    @Override
    public List<Reserva> listarReservasPorSalaEDia(Long idSala, LocalDateTime dataInicio, LocalDateTime dataFim) {
        
        return reservaRepository.buscarReservasPorIntervaloAndStatusAgendado(idSala, dataInicio, dataFim);
    }

    @Override
    public Page<Reserva> listarReservasPorSalaEDiaPaginada(Long idSala, EnumStatusAgendamento status, Integer paginaAtual, Integer tamanhoPagina, String direcao,
                                                           String ordenacao) {
        Sort sort = Sort.by(Sort.Direction.fromString(direcao), ordenacao);
        PageRequest pageable = PageRequest.of(paginaAtual, tamanhoPagina, sort);
        return reservaRepository.buscarReservasPorIntervaloAndStatus(idSala, status, pageable);
    }

    @Override
    public Reserva editarReserva(Long idReserva, Reserva reservaAtualizada) throws Exception {

        Reserva reservaExistente = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com o ID: " + idReserva));

        if(reservaExistente.getStatusAgendamento() == EnumStatusAgendamento.CANCELADO) {
            throw new IllegalStateException("Não é possível editar uma reserva cancelada.");
        }
        
        agendamentoValidador.validaAluguelNoPadraoDosSlots(reservaAtualizada.getDataInicioReserva().toLocalTime(), reservaAtualizada.getDataFimReserva().toLocalTime());
        agendamentoValidador.validaTerminoAnteriorAoInicio(reservaAtualizada.getDataInicioReserva().toLocalTime(), reservaAtualizada.getDataFimReserva().toLocalTime());
        agendamentoValidador.validaTempoMinimo(reservaAtualizada.getDataInicioReserva().toLocalTime(), reservaAtualizada.getDataFimReserva().toLocalTime());
        agendamentoValidador.validaHorarioComercial(reservaAtualizada.getDataInicioReserva().toLocalTime(), reservaAtualizada.getDataFimReserva().toLocalTime());

        agendamentoValidador.validaConflitoReservas(reservaAtualizada);
        agendamentoValidador.validaAluguelNoPassado(reservaAtualizada.getDataInicioReserva());

        reservaExistente.setDataInicioReserva(reservaAtualizada.getDataInicioReserva());
        reservaExistente.setDataFimReserva(reservaAtualizada.getDataFimReserva());
        return reservaRepository.save(reservaExistente);
    }

    
}
