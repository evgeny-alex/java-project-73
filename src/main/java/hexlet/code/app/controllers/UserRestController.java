package hexlet.code.app.controllers;

import hexlet.code.app.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
public class UserRestController {

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok("User successfully created");
    }

    @GetMapping
    public ResponseEntity<UserDto> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(new UserDto());
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok(List.of(new UserDto()));
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserDto userDto, @PathVariable("id") String id) {
        return ResponseEntity.ok("User successfully updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        return ResponseEntity.ok("User successfully deleted");
    }
}
