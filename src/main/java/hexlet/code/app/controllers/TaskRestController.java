package hexlet.code.app.controllers;

import hexlet.code.app.dto.TaskRequestDto;
import hexlet.code.app.dto.TaskResponseDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    @Autowired
    private TaskService taskService;

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

            TaskResponseDto taskResponseDto = new TaskResponseDto();
            taskResponseDto.setName(task.getName());
            taskResponseDto.setDescription(task.getDescription());
            taskResponseDto.setCreatedAt(task.getCreatedAt());
            // TODO: 20.11.2022 Доделать маппинг пользователей и статуса задачи + проверить установку автора задачи

            return ResponseEntity.ok(taskResponseDto);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
