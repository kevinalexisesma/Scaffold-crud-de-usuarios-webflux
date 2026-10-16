package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.r2dbc.entity.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        UserData data = new UserData(1L, "Ana", "Gomez", "ana@mail.com");
        User user = User.builder().id(1L).name("Ana").lastName("Gomez").email("ana@mail.com").build();

        when(repository.findById(1L)).thenReturn(Mono.just(data));
        when(mapper.map(data, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustReturnEmptyWhenIdNotFound() {
        // No mapper stub: with an empty Mono, toEntity is never called
        when(repository.findById(1L)).thenReturn(Mono.empty());

        Mono<User> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UserData data1 = new UserData(1L, "Ana", "Gomez", "ana@mail.com");
        UserData data2 = new UserData(2L, "Luis", "Perez", "luis@mail.com");
        User user1 = User.builder().id(1L).name("Ana").lastName("Gomez").email("ana@mail.com").build();
        User user2 = User.builder().id(2L).name("Luis").lastName("Perez").email("luis@mail.com").build();

        when(repository.findAll()).thenReturn(Flux.just(data1, data2));
        when(mapper.map(data1, User.class)).thenReturn(user1);
        when(mapper.map(data2, User.class)).thenReturn(user2);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(user1, user2)
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        User example = User.builder().name("Ana").build();
        UserData exampleData = new UserData(null, "Ana", null, null);
        UserData data = new UserData(1L, "Ana", "Gomez", "ana@mail.com");
        User user = User.builder().id(1L).name("Ana").lastName("Gomez").email("ana@mail.com").build();

        // findByExample first converts the User to UserData (toData), then each result back to User (toEntity)
        when(mapper.map(example, UserData.class)).thenReturn(exampleData);
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(data));
        when(mapper.map(data, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findByExample(example);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        User user = User.builder().name("Ana").lastName("Gomez").email("ana@mail.com").build();
        UserData data = new UserData(null, "Ana", "Gomez", "ana@mail.com");
        UserData savedData = new UserData(1L, "Ana", "Gomez", "ana@mail.com");
        User savedUser = User.builder().id(1L).name("Ana").lastName("Gomez").email("ana@mail.com").build();

        // save converts User -> UserData (toData), saves it, and converts the saved row back (toEntity)
        when(mapper.map(user, UserData.class)).thenReturn(data);
        when(repository.save(data)).thenReturn(Mono.just(savedData));
        when(mapper.map(savedData, User.class)).thenReturn(savedUser);

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNext(savedUser)
                .verifyComplete();
    }

    @Test
    void mustDeleteById() {
        when(repository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = repositoryAdapter.deleteById(1L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(repository).deleteById(1L);
    }
}
