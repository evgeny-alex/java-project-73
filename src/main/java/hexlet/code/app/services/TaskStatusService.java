package hexlet.code.app.services;

import hexlet.code.app.dto.TaskStatusRequestDto;
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
    public TaskStatus getTaskStatusById(Integer id) {
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
    public Integer createTaskStatus(TaskStatusRequestDto taskStatusRequestDto) {
        TaskStatus taskStatus = new TaskStatus();

        taskStatus.setName(taskStatusRequestDto.getName());

        taskStatusRepository.save(taskStatus);

        return taskStatus.getId();
    }

    /**
     * Обновление статуса задачи
     *
     * @param taskStatusRequestDto - DTO для обновления нового статус задачb
     */
    public void updateTaskStatus(TaskStatusRequestDto taskStatusRequestDto, Integer id) {
        TaskStatus taskStatus = taskStatusRepository.getById(id);

        taskStatus.setName(taskStatusRequestDto.getName());

        taskStatusRepository.save(taskStatus);
    }

    /**
     * Удаление статуса задачи по ID
     *
     * @param id - ID задачи для удаления
     */
    public void deleteTaskStatus(Integer id) {
        taskStatusRepository.deleteById(id);
    }
}
