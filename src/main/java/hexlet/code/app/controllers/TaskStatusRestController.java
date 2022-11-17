package hexlet.code.app.controllers;

import hexlet.code.app.dto.TaskStatusRequestDto;
import hexlet.code.app.dto.TaskStatusResponseDto;
import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.dto.UserResponseDto;
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
        TaskStatus taskStatus = taskStatusService.getTaskStatusById(Integer.getInteger(id));
        if (taskStatus != null) {
            TaskStatusResponseDto taskStatusResponseDto = new TaskStatusResponseDto();

            taskStatusResponseDto.setId(taskStatus.getId());
            taskStatusResponseDto.setName(taskStatus.getName());
            taskStatusResponseDto.setCreatedAt(taskStatus.getCreatedAt());

            return ResponseEntity.ok(taskStatusResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<TaskStatusResponseDto>> getAllStatus() {
        List<TaskStatus> taskStatusList = taskStatusService.getAllTaskStatusList();
        List<TaskStatusResponseDto> taskStatusResponseDtoList = taskStatusList.stream().map(taskStatus -> {
            TaskStatusResponseDto taskStatusResponseDto = new TaskStatusResponseDto();

            taskStatusResponseDto.setId(taskStatus.getId());
            taskStatusResponseDto.setName(taskStatus.getName());
            taskStatusResponseDto.setCreatedAt(taskStatus.getCreatedAt());

            return taskStatusResponseDto;
        }).toList();
        return ResponseEntity.ok(taskStatusResponseDtoList);
    }

    @PostMapping
    public ResponseEntity<String> createTaskStatus(@RequestBody TaskStatusRequestDto taskStatusRequestDto) {
        Integer id = taskStatusService.createTaskStatus(taskStatusRequestDto);
        return ResponseEntity.ok("Task status successfully created with id = " + id);
    }

    @PutMapping
    public ResponseEntity<String> updateTaskStatus(@RequestBody TaskStatusRequestDto taskStatusRequestDto, @PathVariable("id") String id) {
        taskStatusService.updateTaskStatus(taskStatusRequestDto, Integer.getInteger(id));
        return ResponseEntity.ok("Task status successfully updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTaskStatus(@PathVariable("id") String id) {
        taskStatusService.deleteTaskStatus(Integer.getInteger(id));
        return ResponseEntity.ok("Task status successfully deleted");
    }

}
