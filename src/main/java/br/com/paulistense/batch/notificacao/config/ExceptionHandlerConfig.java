package br.com.paulistense.batch.notificacao.config;

import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import br.com.paulistense.batch.notificacao.controller.v1.model.response.ProblemaResponse;
import br.com.paulistense.batch.notificacao.controller.v1.model.response.ProblemaTypeResponse;
import br.com.paulistense.batch.notificacao.model.exception.EntidadeEmUsoException;
import br.com.paulistense.batch.notificacao.model.exception.EntidadeNaoEncontradaException;
import br.com.paulistense.batch.notificacao.model.exception.NegocioException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerConfig extends ResponseEntityExceptionHandler {
    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
            + "o problema persistir, entre em contato com o administrador do sistema.";

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemaTypeResponse problemType = ProblemaTypeResponse.ERRO_DE_SISTEMA;
        String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

        ProblemaResponse problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

        return this.handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        if (body == null) {
            body = ProblemaResponse.builder()
                    .timestamp(OffsetDateTime.now())
                    .title(((HttpStatus) statusCode)
                            .getReasonPhrase())
                    .status(statusCode.value())
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
        } else if (body instanceof String) {
            body = ProblemaResponse.builder()
                    .timestamp(OffsetDateTime.now())
                    .title((String) body)
                    .status(statusCode.value())
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontrada(AccessDeniedException ex, WebRequest request) {

        HttpStatus status = HttpStatus.FORBIDDEN;
        ProblemaTypeResponse problemType = ProblemaTypeResponse.ACESSO_NEGADO;
        String detail = ex.getMessage();

        ProblemaResponse problem = createProblemBuilder(status, problemType, detail).userMessage(detail)
                .userMessage("Você não possui permissão para executar essa operação.").build();

        return this.handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemaTypeResponse problemType = ProblemaTypeResponse.RECURSO_NAO_ENCONTRADO;
        String detail = ex.getMessage();

        ProblemaResponse problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

        return this.handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        ProblemaTypeResponse problemType = ProblemaTypeResponse.ENTIDADE_EM_USO;
        String detail = ex.getMessage();

        ProblemaResponse problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

        return this.handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocio(NegocioException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemaTypeResponse problemType = ProblemaTypeResponse.ERRO_NEGOCIO;
        String detail = ex.getMessage();

        ProblemaResponse problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

        return this.handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).headers(headers).build();
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = new BindException(ex.getRootCause(), ex.getClass().getName());
        return this.handleValidationInternal(ex, headers, (HttpStatus) status, request, bindingResult);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return this.handleValidationInternal(ex, headers, (HttpStatus) status, request, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ProblemaTypeResponse problemType = ProblemaTypeResponse.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());

        ProblemaResponse problem = createProblemBuilder((HttpStatus) status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

        return this.handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers,
                    (HttpStatus) status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, (HttpStatus) status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, (HttpStatus) status, request);
        }

        ProblemaTypeResponse problemType = ProblemaTypeResponse.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

        ProblemaResponse problem = createProblemBuilder((HttpStatus) status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

        return this.handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemaTypeResponse problemType = ProblemaTypeResponse.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format(
                "A propriedade '%s' não existe. ".concat("Corrija ou remova essa propriedade e tente novamente."),
                path);

        ProblemaResponse problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

        return this.handleExceptionInternal(ex, problem, headers, status, request);
    }

    private String joinPath(List<Reference> references) {
        return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemaTypeResponse problemType = ProblemaTypeResponse.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format(
                "A propriedade '%s' recebeu o valor '%s', "
                        .concat("que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s."),
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        ProblemaResponse problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

        return this.handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers, HttpStatus status,
            WebRequest request, BindingResult bindingResult) {
        ProblemaTypeResponse problemType = ProblemaTypeResponse.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        List<ProblemaResponse.Object> problemObjects = bindingResult.getAllErrors().stream().map(objectError -> {
            String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

            String name = objectError.getObjectName();

            if (objectError instanceof FieldError) {
                name = ((FieldError) objectError).getField();
            }

            return ProblemaResponse.Object.builder().name(name).userMessage(message).build();
        }).collect(Collectors.toList());

        ProblemaResponse problem = createProblemBuilder(status, problemType, detail).userMessage(detail)
                .objects(problemObjects)
                .build();

        return this.handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemaTypeResponse problemType = ProblemaTypeResponse.PARAMETRO_INVALIDO;

        String detail = String.format(
                "O parâmetro de URL '%s' recebeu o valor '%s', "
                        .concat("que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s."),
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ProblemaResponse problema = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

        return this.handleExceptionInternal(ex, problema, headers, status, request);
    }

    private ProblemaResponse.ProblemaResponseBuilder createProblemBuilder(HttpStatus status,
            ProblemaTypeResponse problemType, String detail) {

        return ProblemaResponse.builder().timestamp(OffsetDateTime.now()).status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle()).detail(detail);
    }
}
