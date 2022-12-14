package hexlet.code.app.controllers;

import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.dto.UserResponseDto;
import hexlet.code.app.model.User;
import hexlet.code.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequestDto userDto) {
        Integer id = userService.createUser(userDto);
        return ResponseEntity.ok("User successfully created with id = " + id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") String id) {
        User user = userService.getUserById(Integer.parseInt(id));
        if (user != null) {
            UserResponseDto userResponseDto = userService.entityToResponseDto(user);
            return ResponseEntity.ok(userResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUser() {
        List<User> userList = userService.getAllUserList();
        List<UserResponseDto> userResponseDtoList = userList.stream().map(user ->
                userService.entityToResponseDto(user)
        ).toList();
        return ResponseEntity.ok(userResponseDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserRequestDto userDto, @PathVariable("id") String id) {
        userService.updateUser(userDto, Integer.parseInt(id));
        return ResponseEntity.ok("User successfully updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(Integer.parseInt(id));
        return ResponseEntity.ok("User successfully deleted");
    }
}
