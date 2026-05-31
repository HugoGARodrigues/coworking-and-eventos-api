package com.example.coworking_and_eventos_api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.coworking_and_eventos_api.enums.EnumTipoSala;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalaHorariosLivresResponseDTO {
    private Long idSala;
    private String nomeSala;
    private EnumTipoSala tipoSala;
    private List<String> horariosLivres;
    

}
