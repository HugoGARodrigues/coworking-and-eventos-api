package com.example.coworking_and_eventos_api.rest;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.rest.dto.request.SalaRequestDTO;
import com.example.coworking_and_eventos_api.rest.dto.response.SalaHorariosLivresResponseDTO;
import com.example.coworking_and_eventos_api.rest.dto.response.SalaResponseDTO;
import com.example.coworking_and_eventos_api.rest.factory.SalaRestFactory;
import com.example.coworking_and_eventos_api.service.interfaces.SalaService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;



@RequestMapping("/salas")
@RestController
public class SalaRest {
 
    @Autowired
    private SalaService salaService;

    @Operation(summary = "Cria uma nova sala", description = "Endpoint para criação de uma nova sala")
    @PostMapping("/criar-sala")
    public ResponseEntity<SalaResponseDTO> criarSala(@RequestBody SalaRequestDTO salaDTO)
            throws Exception {
        Sala sala = SalaRestFactory.getEntity(salaDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SalaRestFactory.getResponseDTO(salaService.criarSala(sala)));
    }

    @Operation(summary = "Listar salas", description = "Endpoint para listar salas por dia")
    @GetMapping("/consulta-agenda-diaria-paginado")
    public ResponseEntity<Page<SalaResponseDTO>> listarAgendaDiaria(@RequestParam LocalDateTime data,
                                                       @RequestParam(required = false, defaultValue = "0") Integer paginaAtual,
                                                       @RequestParam(required = false, defaultValue = "10") Integer tamanhoPagina,
                                                       @RequestParam(required = false, defaultValue = "ASC") String direcao,
                                                       @RequestParam(required = false, defaultValue = "nome") String ordenacao) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SalaRestFactory.fromPageEntityToPageDTO(salaService.listarAgendaDiaria(data, paginaAtual, tamanhoPagina, direcao, ordenacao)));
    }

    @Operation(summary = "Listar salas", description = "Endpoint para listar salas por tipo")
    @GetMapping("lista-paginada-horario-disponivel")
    public ResponseEntity<Page<SalaHorariosLivresResponseDTO>> listarSalasLivresPorDia(@RequestParam LocalDateTime data,
                                                       @RequestParam(required = false, defaultValue = "0") Integer paginaAtual,
                                                       @RequestParam(required = false, defaultValue = "10") Integer tamanhoPagina,
                                                       @RequestParam(required = false, defaultValue = "ASC") String direcao,
                                                       @RequestParam(required = false, defaultValue = "nome") String ordenacao) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(salaService.listarSalasEHorariosDisponiveis(data, paginaAtual, tamanhoPagina, direcao, ordenacao));
    }
    



}
