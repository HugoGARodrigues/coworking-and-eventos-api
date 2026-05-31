package com.example.coworking_and_eventos_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumTipoSala;

@DataJpaTest
public class SalaRepositoryTest {

    private final TestEntityManager entityManager;
    private final SalaRepository salaRepository;

    @Autowired
    public SalaRepositoryTest(TestEntityManager entityManager, SalaRepository salaRepository) {
        this.entityManager = entityManager;
        this.salaRepository = salaRepository;
    }

    private EnumTipoSala getTipoSalaMock() {
        return EnumTipoSala.values().length > 0 ? EnumTipoSala.values()[0] : null;
    }

    @Test
    void deveEncontrarSalaPorNome() {
        Sala sala = new Sala(null, "Sala de Reunião A", 10, getTipoSalaMock());
        entityManager.persist(sala);
        entityManager.flush();

        Sala salaEncontrada = salaRepository.findByNome("Sala de Reunião A");

        assertThat(salaEncontrada).isNotNull();
        assertThat(salaEncontrada.getNome()).isEqualTo("Sala de Reunião A");
        assertThat(salaEncontrada.getCapacidade()).isEqualTo(10);
    }

    @Test
    void deveListarPaginadoComFiltroDeNomeParcial() {
        Sala sala1 = new Sala(null, "Auditório Principal", 50, getTipoSalaMock());
        Sala sala2 = new Sala(null, "Sala Compartilhada", 20, getTipoSalaMock());
        entityManager.persist(sala1);
        entityManager.persist(sala2);
        entityManager.flush();

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Sala> resultado = salaRepository.listarPaginadoComFiltroDeDiaDaReserva(null, null, "Auditório", pageable);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getNome()).isEqualTo("Auditório Principal");
    }
}