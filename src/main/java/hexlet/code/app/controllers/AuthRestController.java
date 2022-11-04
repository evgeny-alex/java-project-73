package hexlet.code.app.controllers;

import hexlet.code.app.dto.LoginRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class AuthRestController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        // TODO: 04.11.2022 Доделать реализацию генерации токена
        return ResponseEntity.ok("");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register() {
        // TODO: 04.11.2022 Реализовать метод
        return ResponseEntity.ok("");
    }
}
