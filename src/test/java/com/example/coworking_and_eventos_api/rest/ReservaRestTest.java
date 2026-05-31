package com.example.coworking_and_eventos_api.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import com.example.coworking_and_eventos_api.rest.dto.request.ReservaRequestDTO;
import com.example.coworking_and_eventos_api.service.interfaces.ReservaService;
import com.example.coworking_and_eventos_api.service.interfaces.SalaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ReservaRest.class)
public class ReservaRestTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public ReservaRestTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @MockBean
    private ReservaService reservaService;

    @MockBean
    private SalaService salaService;

    @Test
    void deveCriarReservaComSucesso() throws Exception {
        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setDataInicioReserva(LocalDateTime.of(2030, 10, 10, 8, 0));
        dto.setDataFimReserva(LocalDateTime.of(2030, 10, 10, 9, 0));

        Sala sala = new Sala(1L, "Sala Executiva", 10, null);
        when(salaService.buscarSalaPorId(1L)).thenReturn(sala);

        Reserva reserva = new Reserva(10L, 1L, dto.getDataInicioReserva(), dto.getDataFimReserva(), sala);
        reserva.setStatusAgendamento(EnumStatusAgendamento.AGENDADO);
        
        when(reservaService.cadastrarReserva(any(Reserva.class), eq(sala))).thenReturn(reserva);

        mockMvc.perform(post("/reservas/criar-reserva")
                .param("salaId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.statusAgendamento").value("AGENDADO"));
    }

    @Test
    void deveCancelarReservaComSucesso() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setId(10L);
        reserva.setStatusAgendamento(EnumStatusAgendamento.CANCELADO);

        when(reservaService.cancelarReserva(10L)).thenReturn(reserva);

        mockMvc.perform(patch("/reservas/deletar-reserva/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusAgendamento").value("CANCELADO"));
    }

    @Test
    void deveListarReservasPorSalaEDia() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setId(10L);
        reserva.setStatusAgendamento(EnumStatusAgendamento.AGENDADO);

        PageImpl<Reserva> page = new PageImpl<>(List.of(reserva), PageRequest.of(0, 10), 1);

        when(reservaService.listarReservasPorSalaEDiaPaginada(eq(1L), any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        mockMvc.perform(get("/reservas/listar-reservas")
                .param("salaId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(10));
    }

    @Test
    void deveEditarReserva() throws Exception {
        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setDataInicioReserva(LocalDateTime.of(2030, 10, 10, 9, 0));
        dto.setDataFimReserva(LocalDateTime.of(2030, 10, 10, 10, 0));

        Reserva reserva = new Reserva();
        reserva.setId(10L);
        reserva.setDataInicioReserva(dto.getDataInicioReserva());
        reserva.setDataFimReserva(dto.getDataFimReserva());
        reserva.setStatusAgendamento(EnumStatusAgendamento.AGENDADO);

        when(reservaService.editarReserva(eq(10L), any(Reserva.class))).thenReturn(reserva);

        mockMvc.perform(patch("/reservas/editar-reserva/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }
}