package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskRequestDto {

    private String name;

    private String description;

    private Integer taskStatusId;

    private Integer authorId;

    private Integer executorId;

    private List<Integer> labelIds;
}
