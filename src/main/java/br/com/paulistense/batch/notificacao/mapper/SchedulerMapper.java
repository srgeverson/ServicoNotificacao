package br.com.paulistense.batch.notificacao.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.paulistense.batch.notificacao.controller.v1.model.request.ScheduleRequest;
import br.com.paulistense.batch.notificacao.controller.v1.model.response.SchedulerResponse;
import br.com.paulistense.batch.notificacao.model.Scheduler;

@Component
public class SchedulerMapper {
	@Autowired
	private ModelMapper modelMapper;

	public SchedulerResponse toResponse(Scheduler entity) {
		return modelMapper.map(entity, SchedulerResponse.class);
	}

	public Scheduler toRequest(ScheduleRequest request) {
		return modelMapper.map(request, Scheduler.class);
	}

	public List<SchedulerResponse> toListResponse(List<Scheduler> entities) {
		if (entities == null)
			return Collections.emptyList();

		return entities.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
}
