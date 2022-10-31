package hexlet.code.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
public final class WelcomeRestController {

    @GetMapping("")
    public String welcome() {
        return "Hello world";
    }
}
