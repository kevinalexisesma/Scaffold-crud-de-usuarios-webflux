package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.UserRequest;
import co.com.bancolombia.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserRequest request){
        return User.builder()
                .name(request.name())
                .lastName(request.lastName())
                .email(request.email())
                .build();
    }
}
