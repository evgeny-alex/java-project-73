package hexlet.code.app.controllers;

import hexlet.code.app.dto.TaskRequestDto;
import hexlet.code.app.dto.TaskResponseDto;
import hexlet.code.app.dto.TaskSearchCriteria;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.services.TaskService;
import hexlet.code.app.services.TaskStatusService;
import hexlet.code.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskStatusService taskStatusService;

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
        Object principal = auth.getPrincipal();
        User user = (principal instanceof User) ? (User) principal : null;
        if (user != null) {
            Task task = taskService.createTask(taskRequestDto, user.getId());
            TaskResponseDto taskResponseDto = taskService.entityToResponseDto(task);
            return ResponseEntity.ok(taskResponseDto);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @Operation(summary = "Операция получения задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращен DTO задачи"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при получении задачи")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable("id") String id) {
        Task task = taskService.getTaskById(Integer.parseInt(id));
        if (task != null) {
            TaskResponseDto taskResponseDto = taskService.entityToResponseDto(task);
            return ResponseEntity.ok(taskResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Операция обновления задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при обновлении задачи")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskRequestDto taskRequestDto, @PathVariable("id") String id) {
        Task task = taskService.updateTask(taskRequestDto, Integer.parseInt(id));
        if (task != null) {
            TaskResponseDto taskResponseDto = taskService.entityToResponseDto(task);
            return ResponseEntity.ok(taskResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Операция удаления задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при удалении задачи")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") String id) {
        taskService.deleteTask(Integer.parseInt(id));
        return ResponseEntity.ok("Task successfully deleted");
    }

    @Operation(summary = "Операция фильтрации задач")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен отфильтрованный список задач"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при фильтрации задач")
    })
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> filterTasks(@RequestParam(required = false) Integer taskStatus,
                                                      @RequestParam(required = false) Integer executorId,
                                                      @RequestParam(required = false) Integer labels,
                                                      @RequestParam(required = false) Integer authorId) {
        List<Task> taskList = taskService.findWithSearchCriteria(new TaskSearchCriteria(taskStatus, executorId, labels, authorId));
        List<TaskResponseDto> taskResponseDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskList)) {
            taskResponseDtoList = taskList.stream().map(task -> taskService.entityToResponseDto(task)).toList();
        }
        return ResponseEntity.ok(taskResponseDtoList);
    }
}
