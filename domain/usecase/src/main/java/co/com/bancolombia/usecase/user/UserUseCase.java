package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> save(User user){
        return userRepository.save(user);
    }

    public Mono<User> findById(Long id){
        return userRepository.findById(id);
    }

    public Mono<Void> deleteById(Long id){
        return userRepository.deleteById(id);
    }
}
