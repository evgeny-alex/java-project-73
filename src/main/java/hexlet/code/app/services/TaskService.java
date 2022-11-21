package hexlet.code.app.services;

import hexlet.code.app.dto.TaskRequestDto;
import hexlet.code.app.dto.TaskResponseDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import org.apache.tomcat.util.threads.TaskThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * Получение задачи по ID
     *
     * @param id - ID задачи
     * @return - сущность задачи
     */
    public Task getTaskById(Integer id) {
        return taskRepository.getById(id);
    }

    /**
     * Получение списка всех задач
     *
     * @return - список всех задач
     */
    public List<Task> getAllTaskList() {
        return taskRepository.findAll();
    }

    /**
     * Обновление задачи
     *
     * @param taskDto - DTO задачи
     * @param id - ID задачи
     */
    public void updateTask(TaskRequestDto taskDto, Integer id) {
        Task task = taskRepository.getById(id);

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(taskStatusService.getTaskStatusById(taskDto.getTaskStatus()));
        task.setExecutor(userService.getUserById(taskDto.getExecutor()));
        task.setAuthor(userService.getUserById(taskDto.getAuthor()));

        taskRepository.save(task);
    }

    /**
     * Удаление задачи
     *
     * @param id - ID задачи
     */
    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }

    /**
     * Преобразование сущности задачи в DTO ответа
     *
     * @param task - сущность задачи
     * @return - DTO ответа
     */
    public TaskResponseDto entityToResponseDto(Task task) {
        TaskResponseDto taskResponseDto = new TaskResponseDto();

        taskResponseDto.setName(task.getName());
        taskResponseDto.setDescription(task.getDescription());
        taskResponseDto.setCreatedAt(task.getCreatedAt());
        taskResponseDto.setAuthor(userService.entityToResponseDto(task.getAuthor()));
        taskResponseDto.setExecutor(userService.entityToResponseDto(task.getExecutor()));
        taskResponseDto.setTaskStatus(taskStatusService.entityToResponseDto(task.getTaskStatus()));

        return taskResponseDto;
    }
}
