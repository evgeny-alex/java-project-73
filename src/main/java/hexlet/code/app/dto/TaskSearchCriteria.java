package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskSearchCriteria {
    private Integer taskStatus;
    private Integer executorId;
    private Integer labels;
    private Integer authorId;
}
