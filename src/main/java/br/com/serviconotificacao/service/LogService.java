package br.com.serviconotificacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serviconotificacao.model.LogModel;
import br.com.serviconotificacao.repository.LogRepository;

@Service
public class LogService {

	@Autowired
	private LogRepository logRepository;

	public List<LogModel> consultarLogs() {
		return logRepository.findAll();
	}
}
