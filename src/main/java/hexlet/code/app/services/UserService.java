package hexlet.code.app.services;

import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public Integer createUser(UserRequestDto userRequestDto) {
        User user = new User();

        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setCreatedAt(new Date());
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
    public User getUserById(Integer id) {
        return userRepository.getById(id);
    }

    /**
     * Получение списка всех пользователей
     *
     * @return - список все пользователей
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
    public void updateUser(UserRequestDto userRequestDto, Integer id) {
        User user = userRepository.getById(id);

        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        userRepository.save(user);
    }

    /**
     * Удаление пользователя по ID
     *
     * @param id - ID пользователя
     */
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}