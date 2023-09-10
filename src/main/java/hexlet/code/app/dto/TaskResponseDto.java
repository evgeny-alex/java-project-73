package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskResponseDto {

    private Long id;

    private String name;

    private String description;

    private TaskStatusResponseDto taskStatus;

    private UserResponseDto author;

    private UserResponseDto executor;

    private Date createdAt;

}
