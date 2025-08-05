package br.com.paulistense.batch.notificacao.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.paulistense.batch.notificacao.controller.v1.model.request.SchedulerRequest;
import br.com.paulistense.batch.notificacao.controller.v1.model.response.SchedulerResponse;
import br.com.paulistense.batch.notificacao.mapper.SchedulerMapper;
import br.com.paulistense.batch.notificacao.model.Scheduler;
import br.com.paulistense.batch.notificacao.model.exception.EntidadeEmUsoException;
import br.com.paulistense.batch.notificacao.model.exception.EntidadeNaoEncontradaException;
import br.com.paulistense.batch.notificacao.model.exception.NegocioException;
import br.com.paulistense.batch.notificacao.service.SchedulerService;
import br.com.paulistense.batch.notificacao.service.SistemaService;
import br.com.paulistense.batch.notificacao.service.UsuarioService;
import jakarta.validation.Valid;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(path = "/v1/schedulers", produces = MediaType.APPLICATION_JSON_VALUE)
public class SchedulerController {
    @Autowired
    private SchedulerService service;
    @Autowired
    private SchedulerMapper mapper;
    @Autowired
    private SistemaService sistemaService;
    @Autowired
    private UsuarioService usuarioService;
    private static final String PATH = "/v1/schedulers";
    private ProblemDetail problemDetail;

    @GetMapping
    public ResponseEntity<List<SchedulerResponse>> getAllSchedules() {
        try {
            var schedulers = service.buscarTodos();
            var response = mapper.toListResponse(schedulers);
            return ResponseEntity.ok(response);
        } catch (NegocioException ne) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(400), ne.getMessage());
            problemDetail.setTitle("Bad Request");
            problemDetail.setType(URI.create(PATH.concat("/")));
            return ResponseEntity.of(problemDetail).build();
        } catch (Exception e) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage());
            problemDetail.setTitle("Internal server error");
            problemDetail.setType(URI.create(PATH.concat("")));
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<SchedulerResponse> getSchedule(@PathVariable String name) {
        try {
            var schedule = service.buscarPorNome(name)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException(Scheduler.class, name));
            var response = mapper.toResponse(schedule);
            return ResponseEntity.ok(response);
        } catch (NegocioException ne) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(400), ne.getMessage());
            problemDetail.setTitle("Bad Request");
            problemDetail.setType(URI.create(PATH.concat("/")));
            return ResponseEntity.of(problemDetail).build();
        } catch (Exception e) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage());
            problemDetail.setTitle("Internal server error");
            problemDetail.setType(URI.create(PATH.concat("/{".concat(name).concat("}"))));
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<SchedulerResponse> createSchedule(@Valid @RequestBody SchedulerRequest request) {
        try {
            if (service.buscarPorNome(request.getNome()).isPresent())
                throw new EntidadeEmUsoException(Scheduler.class, request.getNome());

            // Converte para entidade
            Scheduler model = mapper.toModel(request);

            // Valida entidades relacionadas (se quiser validar antes de salvar)
            sistemaService.buscarSistemaPorId(model.getSistema().getId());
            usuarioService.buscarUsuarioPorId(model.getUsuario().getId());
            // Salva no banco
            model = service.salvar(model);

            // Mapeia de volta para resposta
            SchedulerResponse response = mapper.toResponse(model);

            // Recarrega tarefas agendadas (se necessário)
            service.carregarTarefas();

            return ResponseEntity.ok(response);

        } catch (NegocioException ne) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(400), ne.getMessage());
            problemDetail.setTitle("Bad Request");
            problemDetail.setType(URI.create(PATH.concat("/")));
            return ResponseEntity.of(problemDetail).build();
        } catch (Exception e) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage());
            problemDetail.setTitle("Internal server error");
            problemDetail.setType(URI.create(PATH.concat("")));
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity<Scheduler> updateSchedule(@PathVariable String name,
            @RequestBody Scheduler config) {
        try {
            Scheduler existing = service.buscarPorNome(name)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Schedule not found"));

            existing.setCronExpression(config.getCronExpression());
            existing.setAtivo(config.getAtivo());
            existing.setDescricao(config.getDescricao());

            Scheduler updated = service.salvar(existing);
            service.carregarTarefas();
            return ResponseEntity.ok(updated);
        } catch (NegocioException ne) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(400), ne.getMessage());
            problemDetail.setTitle("Bad Request");
            problemDetail.setType(URI.create(PATH.concat("/")));
            return ResponseEntity.of(problemDetail).build();
        } catch (Exception e) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage());
            problemDetail.setTitle("Internal server error");
            problemDetail.setType(URI.create(PATH.concat("/{".concat(name).concat("}"))));
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @PutMapping("/{name}/cron")
    public ResponseEntity<Void> updateCronExpression(@PathVariable String name,
            @RequestBody String cronExpression) {
        try {
            service.atualizarCron(name, cronExpression);
            return ResponseEntity.ok().build();
        } catch (NegocioException ne) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(400), ne.getMessage());
            problemDetail.setTitle("Bad Request");
            problemDetail.setType(URI.create(PATH.concat("/")));
            return ResponseEntity.of(problemDetail).build();
        } catch (Exception e) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage());
            problemDetail.setTitle("Internal server error");
            problemDetail.setType(URI.create(PATH.concat("/{".concat(name).concat("}/cron"))));
            return ResponseEntity.of(problemDetail).build();
        }

    }

    @PutMapping("/{name}/toggle")
    public ResponseEntity<Void> toggleSchedule(@PathVariable String name, @RequestParam boolean enabled) {
        try {
            service.ativarOuDesativar(name, enabled);
            return ResponseEntity.ok().build();
        } catch (NegocioException ne) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(400), ne.getMessage());
            problemDetail.setTitle("Bad Request");
            problemDetail.setType(URI.create(PATH.concat("/")));
            return ResponseEntity.of(problemDetail).build();
        } catch (Exception e) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage());
            problemDetail.setTitle("Internal server error");
            problemDetail.setType(URI.create(PATH.concat("/{".concat(name).concat("}/toggle"))));
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @PostMapping("/reload")
    public ResponseEntity<Void> reloadAllSchedules() {
        try {
            service.carregarTarefas();
            return ResponseEntity.ok().build();
        } catch (NegocioException ne) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(400), ne.getMessage());
            problemDetail.setTitle("Bad Request");
            problemDetail.setType(URI.create(PATH.concat("/")));
            return ResponseEntity.of(problemDetail).build();
        } catch (Exception e) {
            problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage());
            problemDetail.setTitle("Internal server error");
            problemDetail.setType(URI.create(PATH.concat("/reload")));
            return ResponseEntity.of(problemDetail).build();
        }
    }
}
