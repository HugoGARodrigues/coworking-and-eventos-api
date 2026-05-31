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

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.entity.Sala;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import com.example.coworking_and_eventos_api.enums.EnumTipoSala;

@DataJpaTest
public class ReservaRepositoryTest {

    private final TestEntityManager entityManager;
    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaRepositoryTest(TestEntityManager entityManager, ReservaRepository reservaRepository) {
        this.entityManager = entityManager;
        this.reservaRepository = reservaRepository;
    }

    private EnumTipoSala getTipoSalaMock() {
        return EnumTipoSala.values().length > 0 ? EnumTipoSala.values()[0] : null;
    }

    @Test
    void deveBuscarReservasPorIntervaloAndStatusAgendado() {
        Sala sala = new Sala(null, "Sala Executiva", 5, getTipoSalaMock());
        sala = entityManager.persist(sala);

        LocalDateTime inicio = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0);
        LocalDateTime fim = inicio.plusHours(1);

        Reserva reserva = new Reserva(null, 100L, inicio, fim, sala);
        reserva.setStatusAgendamento(EnumStatusAgendamento.AGENDADO);
        entityManager.persist(reserva);
        entityManager.flush();

        List<Reserva> encontradas = reservaRepository.buscarReservasPorIntervaloAndStatusAgendado(sala.getId(), inicio, fim);

        assertThat(encontradas).hasSize(1);
        assertThat(encontradas.get(0).getSala().getId()).isEqualTo(sala.getId());
        assertThat(encontradas.get(0).getStatusAgendamento()).isEqualTo(EnumStatusAgendamento.AGENDADO);
    }

    @Test
    void deveBuscarReservasPorIntervaloEStatusPaginado() {
        Sala sala = new Sala(null, "Sala de Eventos", 100, getTipoSalaMock());
        sala = entityManager.persist(sala);

        LocalDateTime inicio = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);
        LocalDateTime fim = inicio.plusHours(2);

        Reserva reserva = new Reserva(null, 200L, inicio, fim, sala);
        reserva.setStatusAgendamento(EnumStatusAgendamento.AGENDADO);
        entityManager.persist(reserva);
        entityManager.flush();

        Page<Reserva> paginaResultado = reservaRepository.buscarReservasPorIntervaloAndStatus(
                sala.getId(), EnumStatusAgendamento.AGENDADO, PageRequest.of(0, 10));

        assertThat(paginaResultado.getContent()).hasSize(1);
        assertThat(paginaResultado.getTotalElements()).isEqualTo(1);
    }
}