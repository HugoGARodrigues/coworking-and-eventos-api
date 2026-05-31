package com.example.coworking_and_eventos_api.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import com.example.coworking_and_eventos_api.rest.dto.request.ReservaRequestDTO;
import com.example.coworking_and_eventos_api.rest.dto.response.ReservaResponseDTO;
import com.example.coworking_and_eventos_api.rest.factory.ReservaRestFactory;
import com.example.coworking_and_eventos_api.service.interfaces.ReservaService;
import com.example.coworking_and_eventos_api.service.interfaces.SalaService;

import io.swagger.v3.oas.annotations.Operation;

@RequestMapping("/reservas")
@RestController
public class ReservaRest {

    private final ReservaService reservaService;

    private final SalaService salaService;

    public ReservaRest(ReservaService reservaService, SalaService salaService) {
        this.reservaService = reservaService;
        this.salaService = salaService;
    }



    @Operation(summary = "Cria uma nova reserva", description = "Endpoint para criação de uma nova reserva em uma sala específica.")
    @PostMapping("/criar-reserva")
    public ResponseEntity<ReservaResponseDTO> criarReserva(@RequestBody ReservaRequestDTO reservaDTO, @RequestParam Long salaId)
            throws Exception {
        Sala sala = salaService.buscarSalaPorId(salaId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReservaRestFactory.getResponseDTO(reservaService.cadastrarReserva(ReservaRestFactory.getEntity(reservaDTO), sala)));
    }

    @PatchMapping("deletar-reserva/{idReserva}")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(@PathVariable Long idReserva) {
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(ReservaRestFactory.getResponseDTO(reservaService.cancelarReserva(idReserva)));
    }

    @Operation(summary = "Listar reservas por sala e dia", description = "Endpoint para listar reservas de uma sala específica em um dia específico.")
    @GetMapping("/listar-reservas")
    public ResponseEntity<Page<ReservaResponseDTO>> listarReservasPorSalaEDia(@RequestParam Long salaId, 
                                                                              @RequestParam(required = false) EnumStatusAgendamento status,
                                                                              @RequestParam(required = false, defaultValue = "0") Integer paginaAtual,
                                                                              @RequestParam(required = false, defaultValue = "10") Integer tamanhoPagina,
                                                                              @RequestParam(required = false, defaultValue = "ASC") String direcao,
                                                                              @RequestParam(required = false, defaultValue = "nome") String ordenacao
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ReservaRestFactory.fromPageEntityToPageDTO(reservaService.listarReservasPorSalaEDiaPaginada(salaId, status, paginaAtual, tamanhoPagina, direcao, ordenacao)));
    }

    @Operation(summary = "Editar reserva", description = "Endpoint para editar uma reserva existente.")
    @PatchMapping("/editar-reserva/{idReserva}")
    public ResponseEntity<ReservaResponseDTO> editarReserva(@PathVariable Long idReserva, @RequestBody ReservaRequestDTO reservaDTO)
            throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ReservaRestFactory.getResponseDTO(reservaService.editarReserva(idReserva, ReservaRestFactory.getEntity(reservaDTO))));
    }


}
