package hexlet.code.app.controllers;

import hexlet.code.app.dto.TaskStatusRequestDto;
import hexlet.code.app.dto.TaskStatusResponseDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.services.TaskStatusService;
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

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> getStatus(@PathVariable("id") String id) {
        TaskStatus taskStatus = taskStatusService.getTaskStatusById(Integer.parseInt(id));
        if (taskStatus != null) {
            TaskStatusResponseDto taskStatusResponseDto = taskStatusService.entityToResponseDto(taskStatus);
            return ResponseEntity.ok(taskStatusResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<TaskStatusResponseDto>> getAllStatus() {
        List<TaskStatus> taskStatusList = taskStatusService.getAllTaskStatusList();
        List<TaskStatusResponseDto> taskStatusResponseDtoList = taskStatusList.stream().map(taskStatus ->
            taskStatusService.entityToResponseDto(taskStatus)
        ).toList();
        return ResponseEntity.ok(taskStatusResponseDtoList);
    }

    @PostMapping
    public ResponseEntity<TaskStatusResponseDto> createTaskStatus(@RequestBody TaskStatusRequestDto taskStatusRequestDto) {
        TaskStatus taskStatus = taskStatusService.createTaskStatus(taskStatusRequestDto);
        if (taskStatus != null) {
            TaskStatusResponseDto taskStatusResponseDto = taskStatusService.entityToResponseDto(taskStatus);
            return ResponseEntity.ok(taskStatusResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusResponseDto> updateTaskStatus(@RequestBody TaskStatusRequestDto taskStatusRequestDto, @PathVariable("id") String id) {
        TaskStatus taskStatus = taskStatusService.updateTaskStatus(taskStatusRequestDto, Integer.parseInt(id));
        if (taskStatus != null) {
            TaskStatusResponseDto taskStatusResponseDto = taskStatusService.entityToResponseDto(taskStatus);
            return ResponseEntity.ok(taskStatusResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskStatus(@PathVariable("id") String id) {
        taskStatusService.deleteTaskStatus(Integer.parseInt(id));
        return ResponseEntity.ok("Task status successfully deleted");
    }

}
