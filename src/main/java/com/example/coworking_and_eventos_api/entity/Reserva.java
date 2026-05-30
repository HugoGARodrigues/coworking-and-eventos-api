package com.example.coworking_and_eventos_api.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "reserva", schema = "public")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@ManyToOne(fetch = FetchType.LAZY), @JoinColumn(name = "cliente_id", nullable = false), apenas para efeito de mock, ja que nao existe a entidade cliente no projeto.
    @Column(nullable = false) //aqui seria o joincolumn para a entidade cliente, mas como nao existe, deixei apenas a coluna.
    private Long clienteId;

    @Column(name = "data_inicio_reserva", nullable = false)
    private LocalDateTime dataInicioReserva;

    @Column(name = "data_fim_reserva", nullable = false)
    private LocalDateTime dataFimReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento", nullable = false)
    private EnumStatusAgendamento statusAgendamento;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    public Reserva() {
    }

    public Reserva(Long id, Long clienteId, LocalDateTime dataInicioReserva, LocalDateTime dataFimReserva, Sala sala) {
        this.id = id;
        this.clienteId = clienteId;
        this.dataInicioReserva = dataInicioReserva;
        this.dataFimReserva = dataFimReserva;
        this.sala = sala;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getDataInicioReserva() {
        return dataInicioReserva;
    }

    public void setDataInicioReserva(LocalDateTime dataInicioReserva) {
        this.dataInicioReserva = dataInicioReserva;
    }

    public LocalDateTime getDataFimReserva() {
        return dataFimReserva;
    }

    public void setDataFimReserva(LocalDateTime dataFimReserva) {
        this.dataFimReserva = dataFimReserva;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public EnumStatusAgendamento getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(EnumStatusAgendamento statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }
}