package hexlet.code.app.controllers;

import hexlet.code.app.dto.UserResponseDto;
import hexlet.code.app.model.User;
import hexlet.code.app.services.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class TaskStatusRestController {

    @Autowired
    private TaskStatusService taskStatusService;

    // TODO: 16.11.2022 Доделать контроллер и сервис статсусов задач

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getStatus(@PathVariable("id") String id) {
//        User user = userService.getUserById(Integer.getInteger(id));
//        if (user != null) {
//            UserResponseDto userResponseDto = new UserResponseDto();
//
//            userResponseDto.setId(user.getId());
//            userResponseDto.setEmail(user.getEmail());
//            userResponseDto.setFirstName(user.getFirstName());
//            userResponseDto.setLastName(user.getLastName());
//            userResponseDto.setCreatedAt(user.getCreatedAt());
//
//            return ResponseEntity.ok(userResponseDto);
//        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllStatus() {
//        List<User> userList = userService.getAllUserList();
//        List<UserResponseDto> userResponseDtoList = userList.stream().map(user -> {
//            UserResponseDto userResponseDto = new UserResponseDto();
//
//            userResponseDto.setId(user.getId());
//            userResponseDto.setEmail(user.getEmail());
//            userResponseDto.setFirstName(user.getFirstName());
//            userResponseDto.setLastName(user.getLastName());
//            userResponseDto.setCreatedAt(user.getCreatedAt());
//
//            return userResponseDto;
//        }).toList();
//        return ResponseEntity.ok(userResponseDtoList);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
