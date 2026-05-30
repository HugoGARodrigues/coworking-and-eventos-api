package com.example.coworking_and_eventos_api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.coworking_and_eventos_api.rest.dto.request.ReservaRequestDTO;
import com.example.coworking_and_eventos_api.rest.dto.response.ReservaResponseDTO;
import com.example.coworking_and_eventos_api.rest.factory.ReservaRestFactory;
import com.example.coworking_and_eventos_api.service.interfaces.ReservaService;

import io.swagger.v3.oas.annotations.Operation;

@RequestMapping("/reservas")
@RestController
public class ReservaRest {

    @Autowired
    private ReservaService reservaService;


    @Operation(summary = "Cria uma nova reserva", description = "Endpoint para criação de uma nova reserva em uma sala específica.")
    @PostMapping("/criar-reserva")
    public ResponseEntity<ReservaResponseDTO> criarReserva(@RequestBody ReservaRequestDTO reservaDTO, @RequestParam Long salaId)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReservaRestFactory.getResponseDTO(reservaService.cadastrarReserva(ReservaRestFactory.getEntity(reservaDTO), salaId)));
    }
    
}
