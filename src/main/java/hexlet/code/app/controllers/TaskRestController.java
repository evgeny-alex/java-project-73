package hexlet.code.app.controllers;

import com.querydsl.core.types.Predicate;
import com.rollbar.notifier.Rollbar;
import hexlet.code.app.dto.TaskRequestDto;
import hexlet.code.app.dto.TaskResponseDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.services.TaskService;
import hexlet.code.app.services.TaskStatusService;
import hexlet.code.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private Rollbar rollbar;

    @Operation(summary = "Операция создания задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при создании задачи")
    })
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto taskRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        User user = userService.getCurrentUser();
        if (user != null) {
            Task task = taskService.createTask(taskRequestDto, user.getId());
            TaskResponseDto taskResponseDto = taskService.entityToResponseDto(task);
            return ResponseEntity.ok(taskResponseDto);
        } else {
            rollbar.error("Произошла ошибка при создании задачи.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Операция получения задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращен DTO задачи"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при получении задачи")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable("id") String id) {
        Task task = taskService.getTaskById(Long.parseLong(id));
        if (task != null) {
            TaskResponseDto taskResponseDto = taskService.entityToResponseDto(task);
            return ResponseEntity.ok(taskResponseDto);
        } else {
            rollbar.error("Произошла ошибка при получении задачи с ID = " + id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Операция обновления задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при обновлении задачи")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskRequestDto taskRequestDto, @PathVariable("id") String id) {
        Task task = taskService.updateTask(taskRequestDto, Long.parseLong(id));
        if (task != null) {
            TaskResponseDto taskResponseDto = taskService.entityToResponseDto(task);
            return ResponseEntity.ok(taskResponseDto);
        }
        rollbar.error("Произошла ошибка при обновлении задачи с ID = " + id);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Операция удаления задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при удалении задачи")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") String id) {
        taskService.deleteTask(Long.parseLong(id));
        return ResponseEntity.ok("Task successfully deleted");
    }

    @Operation(summary = "Операция фильтрации задач")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен отфильтрованный список задач"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при фильтрации задач")
    })
    @GetMapping
    public List<Task> getAllTasks(@QuerydslPredicate final Predicate predicate) {
        return predicate == null ? taskRepository.findAll() : (List<Task>) taskRepository.findAll(predicate);
    }
}
