package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserResponseDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private Date createdAt;
}
