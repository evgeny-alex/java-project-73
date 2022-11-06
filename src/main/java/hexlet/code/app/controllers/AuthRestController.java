package hexlet.code.app.controllers;

import hexlet.code.app.dto.LoginRequestDto;
import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.model.User;
import hexlet.code.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        // TODO: 04.11.2022 Доделать реализацию генерации токена
        return ResponseEntity.ok("");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
        return ResponseEntity.ok("");
    }
}
