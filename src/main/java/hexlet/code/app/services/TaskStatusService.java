package hexlet.code.app.services;

import hexlet.code.app.dto.TaskStatusRequestDto;
import hexlet.code.app.dto.TaskStatusResponseDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    /**
     * Получение статуса задачи по ID
     *
     * @param id - идентификатор статуса задачи
     * @return - сущности статуса задачи
     */
    public TaskStatus getTaskStatusById(Long id) {
        return taskStatusRepository.getById(id);
    }

    /**
     * Получение полного списка статусов задач
     *
     * @return - список статусо задач
     */
    public List<TaskStatus> getAllTaskStatusList() {
        return taskStatusRepository.findAll();
    }

    /**
     * Создание статуса задачи
     *
     * @param taskStatusRequestDto - DTO для создания нового статус задачи
     * @return - ID новой записи
     */
    public TaskStatus createTaskStatus(TaskStatusRequestDto taskStatusRequestDto) {
        TaskStatus taskStatus = new TaskStatus();

        taskStatus.setName(taskStatusRequestDto.getName());

        taskStatusRepository.save(taskStatus);

        return taskStatus;
    }

    /**
     * Обновление статуса задачи
     *
     * @param taskStatusRequestDto - DTO для обновления нового статус задачb
     */
    public TaskStatus updateTaskStatus(TaskStatusRequestDto taskStatusRequestDto, Long id) {
        TaskStatus taskStatus = taskStatusRepository.getById(id);

        taskStatus.setName(taskStatusRequestDto.getName());

        taskStatusRepository.save(taskStatus);

        return taskStatus;
    }

    /**
     * Удаление статуса задачи по ID
     *
     * @param id - ID задачи для удаления
     */
    public void deleteTaskStatus(Long id) {
        taskStatusRepository.deleteById(id);
    }

    /**
     * Преобразование сущности статуса задачи в DTO ответа
     *
     * @param taskStatus - сущность статуса задачи
     * @return - DTO ответа
     */
    public TaskStatusResponseDto entityToResponseDto(TaskStatus taskStatus) {
        TaskStatusResponseDto taskStatusResponseDto = new TaskStatusResponseDto();

        taskStatusResponseDto.setId(taskStatus.getId());
        taskStatusResponseDto.setName(taskStatus.getName());
        taskStatusResponseDto.setCreatedAt(taskStatus.getCreatedAt());

        return taskStatusResponseDto;
    }
}
