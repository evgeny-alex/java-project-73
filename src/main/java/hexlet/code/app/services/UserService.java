package hexlet.code.app.services;

import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String createUser(UserRequestDto userRequestDto) {
        User user = new User();

        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setCreatedAt(new Date());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        userRepository.save(user);

        return String.valueOf(user.getId());
    }
}
