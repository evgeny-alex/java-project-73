package hexlet.code.app.controllers;

import com.rollbar.notifier.Rollbar;
import hexlet.code.app.dto.TaskStatusRequestDto;
import hexlet.code.app.dto.TaskStatusResponseDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.services.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class TaskStatusRestController {

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private Rollbar rollbar;

    @Operation(summary = "Операция получения статуса задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус задачи успешно получен"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при получении статуса задачи")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> getStatus(@PathVariable("id") String id) {
        TaskStatus taskStatus = taskStatusService.getTaskStatusById(Long.parseLong(id));
        if (taskStatus != null) {
            TaskStatusResponseDto taskStatusResponseDto = taskStatusService.entityToResponseDto(taskStatus);
            return ResponseEntity.ok(taskStatusResponseDto);
        }
        rollbar.error("Произошла ошибка при получении статуса задачи с ID = " + id);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Операция получения списка статусов задач")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список статусов задач успешно получен"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при получении списка статусов задач")
    })
    @GetMapping
    public ResponseEntity<List<TaskStatusResponseDto>> getAllStatus() {
        List<TaskStatus> taskStatusList = taskStatusService.getAllTaskStatusList();
        List<TaskStatusResponseDto> taskStatusResponseDtoList = taskStatusList.stream().map(taskStatus ->
            taskStatusService.entityToResponseDto(taskStatus)
        ).toList();
        return ResponseEntity.ok(taskStatusResponseDtoList);
    }

    @Operation(summary = "Операция создания статуса задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус задачи успешно создан"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при создании статуса задачи")
    })
    @PostMapping
    public ResponseEntity<TaskStatusResponseDto> createTaskStatus(@RequestBody TaskStatusRequestDto taskStatusRequestDto) {
        TaskStatus taskStatus = taskStatusService.createTaskStatus(taskStatusRequestDto);
        if (taskStatus != null) {
            TaskStatusResponseDto taskStatusResponseDto = taskStatusService.entityToResponseDto(taskStatus);
            return ResponseEntity.ok(taskStatusResponseDto);
        }
        rollbar.error("Произошла ошибка при создании статуса задачи.");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Операция обновления статуса задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус задачи успешно обновлен"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при обновлении статуса задачи")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> updateTaskStatus(@RequestBody TaskStatusRequestDto taskStatusRequestDto, @PathVariable("id") String id) {
        TaskStatus taskStatus = taskStatusService.updateTaskStatus(taskStatusRequestDto, Long.parseLong(id));
        if (taskStatus != null) {
            TaskStatusResponseDto taskStatusResponseDto = taskStatusService.entityToResponseDto(taskStatus);
            return ResponseEntity.ok(taskStatusResponseDto);
        }
        rollbar.error("Произошла ошибка при обновлении статуса задачи с ID = " + id);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Операция удаления статуса задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус задачи успешно удален"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при удалении статуса задачи")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskStatus(@PathVariable("id") String id) {
        taskStatusService.deleteTaskStatus(Long.parseLong(id));
        return ResponseEntity.ok("Task status successfully deleted");
    }

}
