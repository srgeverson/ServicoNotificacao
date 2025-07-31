package br.com.paulistense.batch.notificacao.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.paulistense.batch.notificacao.controller.v1.model.response.SistemaResponse;
import br.com.paulistense.batch.notificacao.model.Sistema;
import br.com.paulistense.batch.notificacao.model.dto.SistemaDTO;

@Component
public class SistemaMapper {
	 	@Autowired
	    private ModelMapper modelMapper;

	    public SistemaResponse toResponse(Sistema entity) {
	        return modelMapper.map(entity, SistemaResponse.class);
	    }
	    public SistemaResponse toResponse(SistemaDTO dto) {
	        return modelMapper.map(dto, SistemaResponse.class);
	    }
	    public SistemaDTO toDTO(Sistema entity) {
	        return modelMapper.map(entity, SistemaDTO.class);
	    }
}
