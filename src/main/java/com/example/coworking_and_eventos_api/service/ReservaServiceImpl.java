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
import com.example.coworking_and_eventos_api.rest.dto.response.AgendaDiariaResponseDTO;
import com.example.coworking_and_eventos_api.service.interfaces.ReservaService;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    
    private static final Duration TEMPO_MINIMO = Duration.ofHours(1);
    private static final LocalTime INICIO_HORARIO_COMERCIAL = LocalTime.of(8, 0);
    private static final LocalTime FIM_HORARIO_COMERCIAL = LocalTime.of(18, 0);
    

   /*  @Override
    @Transactional
    public Reserva cadastrarReserva(Reserva reserva, Long salaId) {
        validarReserva(salaId, reserva);
            
        sala.getReservas().add(reserva);
        reserva.setSala(sala);
        return reservaRepository.save(reserva);
        
        
    } */

    @Override
    public void validarReserva(Long salaId, Reserva reserva){

        LocalTime inicio = reserva.getDataInicioReserva().toLocalTime();
        LocalTime fim = reserva.getDataFimReserva().toLocalTime();


         if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException("O horário de término não pode ser anterior ao início.");
        }

        if (inicio.isBefore(INICIO_HORARIO_COMERCIAL) || fim.isAfter(FIM_HORARIO_COMERCIAL)) {
            throw new IllegalArgumentException("O aluguel deve estar entre 08:00 e 18:00.");
        }

        Duration duracao = Duration.between(inicio, inicio.isAfter(fim) ? LocalTime.MIDNIGHT : fim);
        if (duracao.compareTo(TEMPO_MINIMO) < 0) {
            throw new IllegalArgumentException("O tempo mínimo de aluguel é de 1 hora.");
        }

        List<Reserva> reservasExistentes = reservaRepository.findReservasBySalaIdAndStatusAgendado(salaId);
        for (Reserva reservaExistente : reservasExistentes) {
            if ((reserva.getDataInicioReserva().isBefore(reservaExistente.getDataFimReserva()) || 
            reserva.getDataInicioReserva().isEqual(reservaExistente.getDataInicioReserva())) &&
                (reserva.getDataFimReserva().isAfter(reservaExistente.getDataInicioReserva()) || 
                reserva.getDataFimReserva().isEqual(reservaExistente.getDataFimReserva()))) {
                throw new IllegalArgumentException("A sala já está reservada para o período solicitado.");
            }
        }
    


    }

    @Override
    public void cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com o ID: " + id));

        reserva.setStatusAgendamento(EnumStatusAgendamento.CANCELADO);
        reservaRepository.save(reserva);
    }

    @Override
    public List<AgendaDiariaResponseDTO> listarReservasPorData(LocalDateTime data) {
        // Implementação para listar reservas por data
       return null;
    }

    
}
