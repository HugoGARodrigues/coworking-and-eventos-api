package com.example.coworking_and_eventos_api.service.interfaces;

import java.util.List;

import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumTipoSala;

public interface SalaService {

    public Sala criarSala(Sala sala);

    public List<Sala> listarSalas(EnumTipoSala tipoSala);

    public Sala buscarSalaPorId(Long id) throws Exception;
    
}
