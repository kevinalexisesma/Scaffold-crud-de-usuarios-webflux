package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
         @NotBlank(message = "name must not be blank") String name,
         String lastName,
         String email
) {
}
