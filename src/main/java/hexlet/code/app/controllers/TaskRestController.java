package hexlet.code.app.controllers;

import hexlet.code.app.dto.TaskRequestDto;
import hexlet.code.app.dto.TaskResponseDto;
import hexlet.code.app.dto.TaskSearchCriteria;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.services.TaskService;
import hexlet.code.app.services.TaskStatusService;
import hexlet.code.app.services.UserService;
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

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable("id") String id) {
        Task task = taskService.getTaskById(Integer.parseInt(id));
        if (task != null) {
            TaskResponseDto taskResponseDto = taskService.entityToResponseDto(task);
            return ResponseEntity.ok(taskResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @GetMapping
//    public ResponseEntity<List<TaskResponseDto>> getAllTask() {
//        List<Task> taskList = taskService.getAllTaskList();
//        List<TaskResponseDto> taskResponseDtoList = taskList.stream().map(task ->
//                taskService.entityToResponseDto(task)
//        ).toList();
//        return ResponseEntity.ok(taskResponseDtoList);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@RequestBody TaskRequestDto taskRequestDto, @PathVariable("id") String id) {
        taskService.updateTask(taskRequestDto, Integer.parseInt(id));
        return ResponseEntity.ok("Task successfully updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTask(@PathVariable("id") String id) {
        taskService.deleteTask(Integer.parseInt(id));
        return ResponseEntity.ok("Task successfully deleted");
    }

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
