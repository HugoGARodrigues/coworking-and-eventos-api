package com.example.coworking_and_eventos_api.rest.factory;

import com.example.coworking_and_eventos_api.entity.Reserva;
import com.example.coworking_and_eventos_api.enums.EnumStatusAgendamento;
import com.example.coworking_and_eventos_api.rest.dto.request.ReservaRequestDTO;
import com.example.coworking_and_eventos_api.rest.dto.response.ReservaResponseDTO;

public class ReservaRestFactory {
    
    public static Reserva getEntity(ReservaRequestDTO reservaDTO) {
        Reserva reserva = new Reserva();
        reserva.setClienteId(reservaDTO.getClienteId());
        reserva.setDataInicioReserva(reservaDTO.getDataInicioReserva());
        reserva.setDataFimReserva(reservaDTO.getDataFimReserva());
        return reserva;
    }

    public static ReservaResponseDTO getResponseDTO(Reserva reserva) {
        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setId(reserva.getId());
        dto.setClienteId(reserva.getClienteId());
        dto.setDataInicioReserva(reserva.getDataInicioReserva());
        dto.setDataFimReserva(reserva.getDataFimReserva());
        dto.setStatusAgendamento(reserva.getStatusAgendamento());
        dto.setSala(reserva.getSala());
        return dto;
    }
}
