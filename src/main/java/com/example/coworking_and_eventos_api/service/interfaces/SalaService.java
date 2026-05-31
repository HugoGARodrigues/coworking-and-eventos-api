package com.example.coworking_and_eventos_api.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumTipoSala;

public interface SalaService {

    public Sala criarSala(Sala sala);

    public List<Sala> listarSalas(EnumTipoSala tipoSala);

    public Sala buscarSalaPorId(Long id) throws Exception;

    public Page<Sala> listarAgendaDiaria(LocalDateTime data, Integer paginaAtual, Integer tamanhoPagina, String direcao, String ordenacao);
}
