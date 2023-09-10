package hexlet.code.app.services;

import hexlet.code.app.dto.TaskRequestDto;
import hexlet.code.app.dto.TaskResponseDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private UserService userService;

    @Autowired
    private LabelService labelService;

    /**
     * Создание задачи
     *
     * @param taskDto  - DTO задачи
     * @param authorId - ID автора задачи
     * @return - сущность задачи
     */
    public Task createTask(TaskRequestDto taskDto, Long authorId) {
        Task task = new Task();

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(taskStatusService.getTaskStatusById(taskDto.getTaskStatusId()));
        task.setExecutor(userService.getUserById(taskDto.getExecutorId()));
        task.setAuthor(userService.getUserById(authorId));
        final Set<Label> labels = Optional.ofNullable(taskDto.getLabelIds())
                .orElse(Set.of())
                .stream()
                .filter(Objects::nonNull)
                .map(Label::new)
                .collect(Collectors.toSet());

        task.setLabels(labels);

        taskRepository.save(task);

        return task;
    }

    /**
     * Получение задачи по ID
     *
     * @param id - ID задачи
     * @return - сущность задачи
     */
    public Task getTaskById(Long id) {
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
     * @param id      - ID задачи
     */
    public Task updateTask(TaskRequestDto taskDto, Long id) {
        Task task = taskRepository.getById(id);

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(taskStatusService.getTaskStatusById(taskDto.getTaskStatusId()));
        task.setExecutor(userService.getUserById(taskDto.getExecutorId()));
        task.setLabels((Set<Label>) taskDto.getLabelIds().stream().map(labelId -> labelService.getLabelById(labelId)).collect(Collectors.toSet()));

        taskRepository.save(task);

        return task;
    }

    /**
     * Удаление задачи
     *
     * @param id - ID задачи
     */
    public void deleteTask(Long id) {
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

        taskResponseDto.setId(task.getId());
        taskResponseDto.setName(task.getName());
        taskResponseDto.setDescription(task.getDescription());
        taskResponseDto.setCreatedAt(task.getCreatedAt());
        taskResponseDto.setAuthor(userService.entityToResponseDto(task.getAuthor()));
        taskResponseDto.setExecutor(userService.entityToResponseDto(task.getExecutor()));
        taskResponseDto.setTaskStatus(taskStatusService.entityToResponseDto(task.getTaskStatus()));

        return taskResponseDto;
    }
}
