package hexlet.code.app.services;

import hexlet.code.app.dto.TaskRequestDto;
import hexlet.code.app.dto.TaskResponseDto;
import hexlet.code.app.dto.TaskSearchCriteria;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static hexlet.code.app.constants.TaskSearchCriteriaConstants.*;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LabelService labelService;

    private CriteriaBuilder criteriaBuilder;

    private CriteriaBuilder getCriteriaBuilder() {
        if (criteriaBuilder == null) {
            criteriaBuilder = entityManager.getCriteriaBuilder();
        }
        return criteriaBuilder;
    }

    /**
     * Создание задачи
     *
     * @param taskDto  - DTO задачи
     * @param authorId - ID автора задачи
     * @return - сущность задачи
     */
    public Task createTask(TaskRequestDto taskDto, Integer authorId) {
        Task task = new Task();

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(taskStatusService.getTaskStatusById(taskDto.getTaskStatusId()));
        task.setExecutor(userService.getUserById(taskDto.getExecutorId()));
        task.setAuthor(userService.getUserById(authorId));
        task.setLabelList(taskDto.getLabelIds().stream().map(labelId -> labelService.getLabelById(labelId)).collect(Collectors.toList()));

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
     * @param id      - ID задачи
     */
    public Task updateTask(TaskRequestDto taskDto, Integer id) {
        Task task = taskRepository.getById(id);

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(taskStatusService.getTaskStatusById(taskDto.getTaskStatusId()));
        task.setExecutor(userService.getUserById(taskDto.getExecutorId()));
        task.setLabelList(taskDto.getLabelIds().stream().map(labelId -> labelService.getLabelById(labelId)).collect(Collectors.toList()));

        taskRepository.save(task);

        return task;
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

        taskResponseDto.setId(task.getId());
        taskResponseDto.setName(task.getName());
        taskResponseDto.setDescription(task.getDescription());
        taskResponseDto.setCreatedAt(task.getCreatedAt());
        taskResponseDto.setAuthor(userService.entityToResponseDto(task.getAuthor()));
        taskResponseDto.setExecutor(userService.entityToResponseDto(task.getExecutor()));
        taskResponseDto.setTaskStatus(taskStatusService.entityToResponseDto(task.getTaskStatus()));

        return taskResponseDto;
    }

    /**
     * Получение задач по критериям поиска
     *
     * @param taskSearchCriteria - критерий поиска
     * @return - список задач
     */
    public List<Task> findWithSearchCriteria(TaskSearchCriteria taskSearchCriteria) {
        // построение запроса
        CriteriaQuery<Task> criteriaQuery = getCriteriaBuilder().createQuery(Task.class);
        Root<Task> root = criteriaQuery.from(Task.class);

        Predicate predicate = buildPredicate(taskSearchCriteria, root);
        criteriaQuery.where(predicate);
        TypedQuery<Task> typedQuery = entityManager.createQuery(criteriaQuery);
        if (Objects.nonNull(taskSearchCriteria.getLabels())) {
            Label label = labelService.getLabelById(taskSearchCriteria.getLabels());
            return typedQuery.getResultList().stream().filter(task -> task.getLabelList().contains(label)).collect(Collectors.toList());
        }
        return typedQuery.getResultList();
    }

    /**
     * Формирование предиката для запроса задач по критерию поиска
     *
     * @param taskSearchCriteria - критерий поиска
     * @param root               - класс-корень запроса
     * @return - итоговый предикат
     */
    private Predicate buildPredicate(TaskSearchCriteria taskSearchCriteria, Root<Task> root) {
        List<Predicate> predicateList = new ArrayList<>();
        if (Objects.nonNull(taskSearchCriteria.getTaskStatus())) {
            predicateList.add(getCriteriaBuilder().equal(root.get(TASK_STATUS), taskSearchCriteria.getTaskStatus()));
        }
        if (Objects.nonNull(taskSearchCriteria.getAuthorId())) {
            predicateList.add(getCriteriaBuilder().equal(root.get(AUTHOR_ID), taskSearchCriteria.getAuthorId()));
        }
        if (Objects.nonNull(taskSearchCriteria.getExecutorId())) {
            predicateList.add(getCriteriaBuilder().equal(root.get(EXECUTOR_ID), taskSearchCriteria.getExecutorId()));
        }
        return getCriteriaBuilder().and(predicateList.toArray(new Predicate[0]));
    }
}
