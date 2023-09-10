package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskRequestDto {

    private String name;

    private String description;

    private Long taskStatusId;

    private Long authorId;

    private Long executorId;

    private Set<Long> labelIds;
}
