package com.example.coworking_and_eventos_api.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.rest.dto.request.SalaRequestDTO;
import com.example.coworking_and_eventos_api.rest.dto.response.SalaHorariosLivresResponseDTO;
import com.example.coworking_and_eventos_api.service.interfaces.SalaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SalaRest.class)
public class SalaRestTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public SalaRestTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @MockBean
    private SalaService salaService;

    @Test
    void deveCriarSalaComSucesso() throws Exception {
        SalaRequestDTO dto = new SalaRequestDTO();
        dto.setNome("Auditório");
        dto.setCapacidade(50);

        Sala sala = new Sala(1L, "Auditório", 50, null);
        when(salaService.criarSala(any(Sala.class))).thenReturn(sala);

        mockMvc.perform(post("/salas/criar-sala")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Auditório"));
    }

    @Test
    void deveEditarSalaComSucesso() throws Exception {
        SalaRequestDTO dto = new SalaRequestDTO();
        dto.setNome("Auditório Editado");

        Sala sala = new Sala(1L, "Auditório Editado", 50, null);
        when(salaService.editarSala(any(Sala.class))).thenReturn(sala);

        mockMvc.perform(post("/salas/editar-sala")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Auditório Editado"));
    }

    @Test
    void deveListarSalas() throws Exception {
        Sala sala = new Sala(1L, "Auditório", 50, null);
        PageImpl<Sala> page = new PageImpl<>(List.of(sala), PageRequest.of(0, 10), 1);

        when(salaService.listarTodasAsSalas(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        mockMvc.perform(get("/salas/listar-salas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Auditório"));
    }

    @Test
    void deveListarAgendaDiaria() throws Exception {
        Sala sala = new Sala(1L, "Auditório", 50, null);
        PageImpl<Sala> page = new PageImpl<>(List.of(sala), PageRequest.of(0, 10), 1);

        when(salaService.listarAgendaDiaria(any(LocalDateTime.class), any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        mockMvc.perform(get("/salas/consulta-agenda-diaria-paginado")
                .param("data", "2023-10-10T08:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Auditório"));
    }

    @Test
    void deveDeletarSala() throws Exception {
        doNothing().when(salaService).deletarSala(1L);

        mockMvc.perform(delete("/salas/deletar-sala")
                .param("id", "1"))
                .andExpect(status().isNoContent());
    }
}