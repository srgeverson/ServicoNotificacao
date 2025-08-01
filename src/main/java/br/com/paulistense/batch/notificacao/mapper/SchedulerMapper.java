package br.com.paulistense.batch.notificacao.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.paulistense.batch.notificacao.controller.v1.model.request.SchedulerRequest;
import br.com.paulistense.batch.notificacao.controller.v1.model.response.SchedulerResponse;
import br.com.paulistense.batch.notificacao.model.Scheduler;
import br.com.paulistense.batch.notificacao.model.Sistema;
import br.com.paulistense.batch.notificacao.model.Usuario;

@Component
public class SchedulerMapper {
	@Autowired
	private ModelMapper modelMapper;

	public SchedulerResponse toResponse(Scheduler entity) {
		return modelMapper.map(entity, SchedulerResponse.class);
	}

	public Scheduler toRequest(SchedulerRequest request) {
		return modelMapper.map(request, Scheduler.class);
	}

	public List<SchedulerResponse> toListResponse(List<Scheduler> entities) {
		if (entities == null)
			return Collections.emptyList();

		return entities.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

    public Scheduler toModel(SchedulerRequest request) {
		return modelMapper.map(request, Scheduler.class);
    }
	
	public void copyToDomainObject(SchedulerRequest request, Scheduler entity) {
		/*
		Para evitar org.hibernate.HibernateException: identifier of an instance of 
		br.com.paulistense.batch.notificacao.model was altered from 1 to 2
		*/ 
		entity.setSistema(new Sistema());
		entity.setUsuario(new Usuario());
		
		modelMapper.map(request, entity);
	}
}
