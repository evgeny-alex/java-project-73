package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskStatusResponseDto {

    private Long id;

    private String name;

    private Date createdAt;
}
