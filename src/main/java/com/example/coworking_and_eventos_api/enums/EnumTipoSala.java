package com.example.coworking_and_eventos_api.enums;

public enum EnumTipoSala {
    SALA_PRIVATIVA("Sala Privada"),
    SALA_COLETIVA("Sala Coletiva"),
    SALA_AUDITORIO_EVENTOS("Auditório/Eventos");

    private String descricao;

    private EnumTipoSala(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
