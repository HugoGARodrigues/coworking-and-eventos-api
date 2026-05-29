package com.example.coworking_and_eventos_api.rest.dto.response;

public class AgendaDiariaResponseDTO {
    private String nomeSala;
    private String data;
    private String horaInicio;
    private String horaFim;

    public AgendaDiariaResponseDTO() {
    }

    public AgendaDiariaResponseDTO(String nomeSala, String data, String horaInicio, String horaFim) {
        this.nomeSala = nomeSala;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public String getNomeSala() {
        return nomeSala;
    }

    public void setNomeSala(String nomeSala) {
        this.nomeSala = nomeSala;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

}
