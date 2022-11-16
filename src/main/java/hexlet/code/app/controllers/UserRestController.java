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
        User user = userService.getUserById(Integer.getInteger(id));
        if (user != null) {
            UserResponseDto userResponseDto = new UserResponseDto();

            userResponseDto.setId(user.getId());
            userResponseDto.setEmail(user.getEmail());
            userResponseDto.setFirstName(user.getFirstName());
            userResponseDto.setLastName(user.getLastName());
            userResponseDto.setCreatedAt(user.getCreatedAt());

            return ResponseEntity.ok(userResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUser() {
        List<User> userList = userService.getAllUserList();
        List<UserResponseDto> userResponseDtoList = userList.stream().map(user -> {
            UserResponseDto userResponseDto = new UserResponseDto();

            userResponseDto.setId(user.getId());
            userResponseDto.setEmail(user.getEmail());
            userResponseDto.setFirstName(user.getFirstName());
            userResponseDto.setLastName(user.getLastName());
            userResponseDto.setCreatedAt(user.getCreatedAt());

            return userResponseDto;
        }).toList();
        return ResponseEntity.ok(userResponseDtoList);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserRequestDto userDto, @PathVariable("id") String id) {
        userService.updateUser(userDto, Integer.getInteger(id));
        return ResponseEntity.ok("User successfully updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(Integer.getInteger(id));
        return ResponseEntity.ok("User successfully deleted");
    }
}
