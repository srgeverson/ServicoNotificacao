package br.com.paulistense.batch.notificacao.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.paulistense.batch.notificacao.controller.v1.model.response.SchedulerResponse;
import br.com.paulistense.batch.notificacao.mapper.SchedulerMapper;
import br.com.paulistense.batch.notificacao.model.Scheduler;
import br.com.paulistense.batch.notificacao.model.exception.EntidadeNaoEncontradaException;
import br.com.paulistense.batch.notificacao.service.SchedulerService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(path = "/v1/schedulers", produces = MediaType.APPLICATION_JSON_VALUE)
public class SchedulerController {
    @Autowired
    private SchedulerService service;
    @Autowired
    private SchedulerMapper mapper;

    @GetMapping
    public ResponseEntity<List<SchedulerResponse>> getAllSchedules() {
        var schedulers = service.buscarTodos();
        var response = mapper.toListResponse(schedulers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Scheduler> getSchedule(@PathVariable String name) {
        var schedule = service.buscarPorNome(name);
        schedule.orElseThrow(() -> new EntidadeNaoEncontradaException("Schedule not found"));
        return ResponseEntity.ok(schedule.get()); 
    }

    @PostMapping
    public ResponseEntity<Scheduler> createSchedule(@RequestBody Scheduler config) {
        if (service.buscarPorNome(config.getNome()).isPresent()) {
            throw new IllegalArgumentException("Schedule with this name already exists");
        }

        Scheduler savedConfig = service.salvar(config);
        service.carregarTarefas();
        return ResponseEntity.ok(savedConfig);
    }

    @PutMapping("/{name}")
    public ResponseEntity<Scheduler> updateSchedule(@PathVariable String name,
            @RequestBody Scheduler config) {
        Scheduler existing = service.buscarPorNome(name)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Schedule not found"));

        existing.setCronExpression(config.getCronExpression());
        existing.setAtivo(config.getAtivo());
        existing.setDescricao(config.getDescricao());

        Scheduler updated = service.salvar(existing);
        service.carregarTarefas();
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{name}/cron")
    public ResponseEntity<Void> updateCronExpression(@PathVariable String name,
            @RequestBody String cronExpression) {
        service.atualizarCron(name, cronExpression);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{name}/toggle")
    public ResponseEntity<Void> toggleSchedule(@PathVariable String name, @RequestParam boolean enabled) {
        service.ativarOuDesativar(name, enabled);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reload")
    public ResponseEntity<Void> reloadAllSchedules() {
        service.carregarTarefas();
        return ResponseEntity.ok().build();
    }
}
