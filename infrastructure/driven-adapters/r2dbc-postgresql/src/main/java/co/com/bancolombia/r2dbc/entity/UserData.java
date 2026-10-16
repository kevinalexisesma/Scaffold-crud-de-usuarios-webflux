package co.com.bancolombia.r2dbc.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserData {
    @Id
    private Long id;

    private String name;
    private String lastName;
    private String email;
}
