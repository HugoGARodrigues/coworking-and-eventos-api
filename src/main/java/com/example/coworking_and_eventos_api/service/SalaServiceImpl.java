package com.example.coworking_and_eventos_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumTipoSala;
import com.example.coworking_and_eventos_api.repository.SalaRepository;
import com.example.coworking_and_eventos_api.rest.dto.response.SalaHorariosLivresResponseDTO;
import com.example.coworking_and_eventos_api.service.interfaces.SalaService;

@Service
public class SalaServiceImpl implements SalaService {

    @Autowired
    private SalaRepository salaRepository;

    private static final List<String> GRADE_HORARIOS_PADRAO = Arrays.asList(
        "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
        "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00",
        "16:00 - 17:00", "17:00 - 18:00"
    );

    @Override
    public Sala criarSala(Sala sala) {
        return salaRepository.save(sala);
    }

    @Override
    public List<Sala> listarSalas(EnumTipoSala tipoSala) {
        return salaRepository.findByTipoSala(tipoSala);
    }

    @Override
    public Sala buscarSalaPorId(Long id) throws Exception {
        return salaRepository.findById(id).orElseThrow(() -> new Exception("Sala não encontrada com o ID: " + id));
    }

     @Override
    public Page<Sala> listarAgendaDiaria(LocalDateTime data, Integer paginaAtual, Integer tamanhoPagina, String direcao,
                                         String ordenacao){
        LocalDateTime inicioDiaNovaReserva = data.toLocalDate().atStartOfDay();
        LocalDateTime fimDiaNovaReserva = data.toLocalDate().atTime(LocalTime.MAX);
        Sort sort = Sort.by(Sort.Direction.fromString(direcao), ordenacao);
        PageRequest pageable = PageRequest.of(paginaAtual, tamanhoPagina, sort);
        Page<Sala> salaPaginado = salaRepository.listarPaginadoComFiltroDeDiaDaReserva(inicioDiaNovaReserva, fimDiaNovaReserva, pageable);
        return salaPaginado;
    }
    
/*

    public List<SalaHorariosLivresResponseDTO> listarSalasEHorariosDisponiveis(LocalDateTime data, Integer paginaAtual, Integer tamanhoPagina, String direcao,
                                         String ordenacao) {
        
        Page<Sala> todasAsSalasComReservaPorDia = listarAgendaDiaria(data, paginaAtual, tamanhoPagina, direcao, ordenacao);

        Map<Sala, List<Reserva>> reservasPorSala = todasAsSalasComReservaPorDia.stream()
                .collect(Collectors.groupingBy(r -> r.getSala().getId()));

        return todasAsSalas.stream()
                .map(sala -> {
                    List<Reserva> reservasDaSala = reservasPorSala.getOrDefault(sala.getId(), Collections.emptyList());
                    
                    // Descobre quais slots da grade estão livres para ESTA sala
                    List<String> slotsLivres = GRADE_HORARIOS_PADRAO.stream()
                            .filter(slot -> !isSlotConflitante(slot, reservasDaSala))
                            .collect(Collectors.toList());

                    return new SalaHorariosLivresDTO(sala, slotsLivres);
                })
                // O PULO DO GATO: Filtra e remove as salas que não possuem NENHUM horário livre no dia
                .filter(dto -> !dto.getHorariosDisponiveis().isEmpty())
                .collect(Collectors.toList());
    }

    private boolean isSlotConflitante(String slot, List<Reserva> reservas) {
        String[] partes = slot.split(" - ");
        LocalTime slotInicio = LocalTime.parse(partes[0]);
        LocalTime slotFim = LocalTime.parse(partes[1]);

        return reservas.stream().anyMatch(reserva -> 
            slotInicio.isBefore(reserva.getHoraFim()) && slotFim.isAfter(reserva.getHoraInicio())
        );
    } */


}
