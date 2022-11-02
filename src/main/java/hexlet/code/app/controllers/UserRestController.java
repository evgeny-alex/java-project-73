package hexlet.code.app.controllers;

import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.dto.UserResponseDto;
import hexlet.code.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    // TODO: 02.11.2022 Реализовать остальные методы в сервисе

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequestDto userDto) {
        String id = userService.createUser(userDto);
        return ResponseEntity.ok("User successfully created with id = " + id);
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(new UserResponseDto());
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUser() {
        return ResponseEntity.ok(List.of(new UserResponseDto()));
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserRequestDto userDto, @PathVariable("id") String id) {
        return ResponseEntity.ok("User successfully updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        return ResponseEntity.ok("User successfully deleted");
    }
}
