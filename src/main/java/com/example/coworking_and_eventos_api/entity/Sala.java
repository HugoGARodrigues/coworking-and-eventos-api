package com.example.coworking_and_eventos_api.entity;

import java.util.List;

import com.example.coworking_and_eventos_api.enums.EnumTipoSala;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "salas", schema = "public")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer capacidade;

    
    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Reserva> reservas;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sala", nullable = false)
    private EnumTipoSala tipoSala;

    public Sala() {
    }

    public Sala(Long id, String nome, Integer capacidade, EnumTipoSala tipoSala) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.tipoSala = tipoSala;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public EnumTipoSala getTipoSala() {
        return tipoSala;
    }

    public void setTipoSala(EnumTipoSala tipoSala) {
        this.tipoSala = tipoSala;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
    
}
