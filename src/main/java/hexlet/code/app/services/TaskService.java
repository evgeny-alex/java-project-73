package hexlet.code.app.services;

import hexlet.code.app.dto.TaskRequestDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private UserService userService;

    /**
     * Создание задачи
     *
     * @param taskDto - DTO задачи
     * @param authorId - ID автора задачи
     * @return - сущность задачи
     */
    public Task createTask(TaskRequestDto taskDto, Integer authorId) {
        Task task = new Task();

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(taskStatusService.getTaskStatusById(taskDto.getTaskStatus()));
        task.setExecutor(userService.getUserById(taskDto.getExecutor()));
        task.setAuthor(userService.getUserById(authorId));

        taskRepository.save(task);

        return task;
    }
}
