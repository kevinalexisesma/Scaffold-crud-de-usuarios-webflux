package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.ApiResponse;
import co.com.bancolombia.api.dto.UserRequest;
import co.com.bancolombia.api.mapper.UserMapper;
import co.com.bancolombia.api.validator.RequestValidator;
import co.com.bancolombia.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final RequestValidator requestValidator;
    private final UserMapper userMapper;

    public Mono<ServerResponse> findUserById(ServerRequest serverRequest) {
        return userUseCase.findById(Long.valueOf(serverRequest.pathVariable("id")))
                .flatMap(user -> respond(HttpStatus.OK, "User found", user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserRequest.class)
                .flatMap(requestValidator::validate)
                .map(userMapper::toUser)
                .flatMap(userUseCase::save)
                .flatMap(user -> respond(HttpStatus.CREATED, "User created",user));
    }

    public Mono<ServerResponse> deleteUserById(ServerRequest serverRequest) {
        return userUseCase.deleteById(Long.valueOf(serverRequest.pathVariable("id")))
                .then(ServerResponse.noContent().build());
    }

    private <T> Mono<ServerResponse> respond(HttpStatus status, String message, T data) {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ApiResponse.of(status, message, data));
    }
}
