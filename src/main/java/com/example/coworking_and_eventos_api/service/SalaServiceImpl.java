package com.example.coworking_and_eventos_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.repository.SalaRepository;
import com.example.coworking_and_eventos_api.service.interfaces.SalaService;

@Service
public class SalaServiceImpl implements SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Override
    public Sala cadastrarSala(Sala sala) {
        return salaRepository.save(sala);
    }

    
}
