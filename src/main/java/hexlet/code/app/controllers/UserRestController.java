package hexlet.code.app.controllers;

import com.rollbar.notifier.Rollbar;
import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.dto.UserResponseDto;
import hexlet.code.app.model.User;
import hexlet.code.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hexlet.code.app.controllers.UserRestController.USER_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url:/api}" + USER_CONTROLLER_PATH)
public class UserRestController {

    public static final String USER_CONTROLLER_PATH = "/users";

    @Autowired
    private UserService userService;

    @Autowired
    private Rollbar rollbar;

    @Operation(summary = "Операция создания пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при создании пользователя")
    })
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequestDto userDto) {
        Long id = userService.createUser(userDto);
        return ResponseEntity.ok(String.valueOf(id));
    }

    @Operation(summary = "Операция получения пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно возвращен"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при получении пользователя")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") String id) {
        User user = userService.getUserById(Long.parseLong(id));
        if (user != null) {
            UserResponseDto userResponseDto = userService.entityToResponseDto(user);
            return ResponseEntity.ok(userResponseDto);
        }
        rollbar.error("Произошла ошибка при получении пользователя по ID = " + id);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Операция получения списка пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователь успешно возвращен"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при получении списка пользователей")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUser() {
        List<User> userList = userService.getAllUserList();
        List<UserResponseDto> userResponseDtoList = userList.stream().map(user ->
                userService.entityToResponseDto(user)
        ).toList();
        return ResponseEntity.ok(userResponseDtoList);
    }

    @Operation(summary = "Операция обновления пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при обновлении пользователя")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserRequestDto userDto, @PathVariable("id") String id) {
        userService.updateUser(userDto, Long.parseLong(id));
        return ResponseEntity.ok("User successfully updated");
    }

    @Operation(summary = "Операция удаления пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при удалении пользователя")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(Long.parseLong(id));
        return ResponseEntity.ok("User successfully deleted");
    }
}
