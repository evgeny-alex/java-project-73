package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}
