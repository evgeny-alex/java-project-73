package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDto {

    private String name;

    private String description;

    private Integer taskStatus;

    private Integer author;

    private Integer executor;
}
