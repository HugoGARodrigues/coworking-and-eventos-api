package com.example.coworking_and_eventos_api.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumTipoSala;
import com.example.coworking_and_eventos_api.repository.SalaRepository;
import com.example.coworking_and_eventos_api.rest.dto.response.SalaHorariosLivresResponseDTO;
import com.example.coworking_and_eventos_api.service.interfaces.ReservaService;
import com.example.coworking_and_eventos_api.service.interfaces.SalaService;

@Service
public class SalaServiceImpl implements SalaService {

    private final SalaRepository salaRepository;
    private final ReservaService reservaService;

    public SalaServiceImpl(SalaRepository salaRepository, @Lazy ReservaService reservaService) {
        this.salaRepository = salaRepository;
        this.reservaService = reservaService;
    }

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
    public Page<Sala> listarAgendaDiaria(LocalDateTime data, String nomeSala , Integer paginaAtual, Integer tamanhoPagina, String direcao,
                                         String ordenacao){
        LocalDateTime inicioDiaNovaReserva = data.toLocalDate().atStartOfDay();
        LocalDateTime fimDiaNovaReserva = data.toLocalDate().atTime(LocalTime.MAX);
        Sort sort = Sort.by(Sort.Direction.fromString(direcao), ordenacao);
        PageRequest pageable = PageRequest.of(paginaAtual, tamanhoPagina, sort);
        Page<Sala> salaPaginado = salaRepository.listarPaginadoComFiltroDeDiaDaReserva(inicioDiaNovaReserva, fimDiaNovaReserva, nomeSala, pageable);
        for (Sala sala : salaPaginado) {
            List<Reserva> reservasDaSala = reservaService.listarReservasPorSalaEDia(sala.getId(), inicioDiaNovaReserva, fimDiaNovaReserva);
            sala.setReservas(reservasDaSala);
        }
        return salaPaginado;
    }
    

    @Override
    public Page<SalaHorariosLivresResponseDTO> listarSalasEHorariosDisponiveis(LocalDateTime data, Integer paginaAtual, Integer tamanhoPagina, String direcao,
                                         String ordenacao) {
        
        Page<Sala> todasAsSalas = salaRepository.findAll(PageRequest.of(paginaAtual, tamanhoPagina, Sort.by(Sort.Direction.fromString(direcao), ordenacao)));

        Map<Long, List<Reserva>> reservasPorSala = todasAsSalas.stream()
                .collect(Collectors.toMap(
                    Sala::getId,
                    sala -> reservaService.listarReservasPorSalaEDia(sala.getId(), data.toLocalDate().atStartOfDay(), data.toLocalDate().atTime(LocalTime.MAX))
                ));

        List<SalaHorariosLivresResponseDTO> conteudo = todasAsSalas.stream()
                .map(sala -> {
                    List<Reserva> reservasDaSala = reservasPorSala.getOrDefault(sala.getId(), Collections.emptyList());
                    
                    List<String> slotsLivres = GRADE_HORARIOS_PADRAO.stream()
                            .filter(slot -> !isSlotConflitante(slot, reservasDaSala))
                            .collect(Collectors.toList());

                    return new SalaHorariosLivresResponseDTO(sala.getId(), sala.getNome(), sala.getTipoSala(), slotsLivres);
                })
                .filter(dto -> !dto.getHorariosLivres().isEmpty())
                .collect(Collectors.toList());

        return new PageImpl<>(conteudo, todasAsSalas.getPageable(), todasAsSalas.getTotalElements());
    }

    @Override
    public boolean isSlotConflitante(String slot, List<Reserva> reservas) {
        String[] partes = slot.split(" - ");
        LocalTime slotInicio = LocalTime.parse(partes[0]).truncatedTo(ChronoUnit.MINUTES);
        LocalTime slotFim = LocalTime.parse(partes[1]).truncatedTo(ChronoUnit.MINUTES);

        
        return reservas.stream().anyMatch(reserva -> {
            LocalTime reservaInicio = reserva.getDataInicioReserva().toLocalTime().truncatedTo(ChronoUnit.MINUTES);
            LocalTime reservaFim = reserva.getDataFimReserva().toLocalTime().truncatedTo(ChronoUnit.MINUTES);

            return slotInicio.equals(reservaInicio) && slotFim.equals(reservaFim);
        });
    }

    @Override
    public void nomeSalaIgualValidador(Sala sala) {
        Sala salaExistente = salaRepository.findByNome(sala.getNome());
        if (salaExistente != null) {
            throw new IllegalArgumentException("Já existe uma sala com o nome: " + sala.getNome());
        }

    }

    @Override
    public void deletarSala(Long id) throws Exception {
        Sala sala = buscarSalaPorId(id);
        salaRepository.delete(sala);
    }

    @Override
    public Page<Sala> listarTodasAsSalas(String filtroNome, Integer paginaAtual, Integer tamanhoPagina, String direcao,
                                         String ordenacao) throws Exception {
                                            Sort sort = Sort.by(Sort.Direction.fromString(direcao), ordenacao);
        PageRequest pageable = PageRequest.of(paginaAtual, tamanhoPagina, sort);
        Page<Sala> salas = salaRepository.listarPaginadoComFiltroDeDiaDaReserva(null, null, filtroNome, pageable);
        return salas;
    }

    @Override
    public Sala editarSala(Sala sala) throws Exception {
        Sala salaExistente = buscarSalaPorId(sala.getId());
        if (salaExistente == null) {
            throw new Exception("Sala não encontrada com o ID: " + sala.getId());
        }
        salaExistente.setNome(sala.getNome());
        salaExistente.setCapacidade(sala.getCapacidade());
        salaExistente.setTipoSala(sala.getTipoSala());
        return salaRepository.save(salaExistente);
    }

}
