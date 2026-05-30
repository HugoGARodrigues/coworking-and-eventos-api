package com.example.coworking_and_eventos_api.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.repository.ReservaRepository;

@Service
public class AgendamentoValidador {

    @Autowired
    private ReservaRepository reservaRepository;

    private static final Duration TEMPO_MINIMO = Duration.ofHours(1);
    private static final LocalTime INICIO_HORARIO_COMERCIAL = LocalTime.of(8, 0);
    private static final LocalTime FIM_HORARIO_COMERCIAL = LocalTime.of(18, 0);

    public void validaTerminoAnteriorAoInicio(LocalTime inicio, LocalTime fim) {
        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException("O horário de término não pode ser anterior ao início.");
        }
    }

    public void validaHorarioComercial(LocalTime inicio, LocalTime fim) {
        if (inicio.isBefore(INICIO_HORARIO_COMERCIAL) || fim.isAfter(FIM_HORARIO_COMERCIAL)) {
            throw new IllegalArgumentException("O aluguel deve estar entre 08:00 e 18:00.");
        }
    }

    public void validaTempoMinimo(LocalTime inicio, LocalTime fim) {
        Duration duracao = Duration.between(inicio, fim);
        if (duracao.compareTo(TEMPO_MINIMO) < 0) {
            throw new IllegalArgumentException("O tempo mínimo de aluguel é de 1 hora.");
        }
    }

    public void validaConflitoReservas(Reserva novaReserva) {
        Long salaId = novaReserva.getSala().getId();
        LocalDateTime inicioDiaNovaReserva = novaReserva.getDataInicioReserva().toLocalDate().atStartOfDay();
        LocalDateTime fimDiaNovaReserva = novaReserva.getDataInicioReserva().toLocalDate().atTime(LocalTime.MAX);
        List<Reserva> reservasExistentes = reservaRepository.buscarReservasPorIntervaloAndStatusAgendado(salaId, inicioDiaNovaReserva, fimDiaNovaReserva);
        for (Reserva reservaExistente : reservasExistentes) {
            if ((novaReserva.getDataInicioReserva().isBefore(reservaExistente.getDataFimReserva()) ||
                 novaReserva.getDataInicioReserva().isEqual(reservaExistente.getDataInicioReserva())) &&
                (novaReserva.getDataFimReserva().isAfter(reservaExistente.getDataInicioReserva()) ||
                 novaReserva.getDataFimReserva().isEqual(reservaExistente.getDataFimReserva()))) {
                throw new IllegalArgumentException("A sala já está reservada para o período solicitado.");
            }
        }
    }

   
}
