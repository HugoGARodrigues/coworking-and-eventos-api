package com.example.coworking_and_eventos_api.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.repository.ReservaRepository;

@Service
public class AgendamentoValidador {

    private static final List<String> GRADE_HORARIOS_PADRAO = Arrays.asList(
        "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
        "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00",
        "16:00 - 17:00", "17:00 - 18:00"
    );

    
    private final ReservaRepository reservaRepository;

    public AgendamentoValidador(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

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

    public void validaAluguelNoPassado(LocalDateTime dataInicio) {
        if (dataInicio.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("O aluguel não pode ser agendado para uma data e hora no passado.");
        }
    }

    public void validaAluguelNoPadraoDosSlots(LocalTime inicio, LocalTime fim) {
        String slot = String.format("%02d:%02d - %02d:%02d", inicio.getHour(), inicio.getMinute(), fim.getHour(), fim.getMinute());
        if (!GRADE_HORARIOS_PADRAO.contains(slot)) {
            throw new IllegalArgumentException("O aluguel deve estar dentro dos slots de horários pré-definidos.");
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
