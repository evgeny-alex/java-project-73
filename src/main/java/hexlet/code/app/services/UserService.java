package hexlet.code.app.services;

import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.dto.UserResponseDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Создание пользователя
     *
     * @param userRequestDto - DTO пользователя
     * @return - Строка с ID пользователя
     */
    public Long createUser(UserRequestDto userRequestDto) {
        User user = new User();

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        userRepository.save(user);

        return user.getId();
    }

    /**
     * Получение пользователя по ID
     *
     * @param id - ID пользователя
     * @return - сущность пользователя
     */
    public User getUserById(Long id) {
        return userRepository.getById(id);
    }

    /**
     * Получение списка всех пользователей
     *
     * @return - список всех пользователей
     */
    public List<User> getAllUserList() {
        return userRepository.findAll();
    }

    /**
     * Обновление пользователя
     *
     * @param userRequestDto - DTO пользователя
     * @param id - ID пользователя
     */
    public void updateUser(UserRequestDto userRequestDto, Long id) {
        User user = userRepository.getById(id);

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        userRepository.save(user);
    }

    /**
     * Удаление пользователя по ID
     *
     * @param id - ID пользователя
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Преобразование объекта сущности пользвателя к DTO ответа
     *
     * @param user - сущность пользователя
     * @return - DTO ответа
     */
    public UserResponseDto entityToResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setCreatedAt(user.getCreatedAt());

        return userResponseDto;
    }

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserEmail()).get();
    }

}
