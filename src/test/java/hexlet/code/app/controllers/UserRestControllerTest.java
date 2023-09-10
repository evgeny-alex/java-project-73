package hexlet.code.app.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.dto.UserResponseDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hexlet.code.app.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void initUser() throws Exception {
        UserRequestDto userRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_default_user.json").getFile(), UserRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)));
    }

    @AfterEach
    public void clearUser() {
        userRepository.deleteAll();
    }

    @Test
    public void getOneUserTest() throws Exception {
        User expectedUser = userRepository.findAll().get(0);
        var response = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/users/" + expectedUser.getId()))
                .andExpect(status().isOk()).andReturn().getResponse();
        UserResponseDto userExpectedDto = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedUser.getEmail(), userExpectedDto.getEmail());
        assertEquals(expectedUser.getId(), userExpectedDto.getId());
        assertEquals(expectedUser.getLastName(), userExpectedDto.getLastName());
        assertEquals(expectedUser.getFirstName(), userExpectedDto.getFirstName());
        assertEquals(expectedUser.getCreatedAt(), userExpectedDto.getCreatedAt());
    }

    @Test
    public void getAllUserTest() throws Exception {
        int size = userRepository.findAll().size();
        var response = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/users"))
                .andExpect(status().isOk()).andReturn().getResponse();
        List<UserResponseDto> userExpectedDtoList = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(size, userExpectedDtoList.size());
    }

    @Test
    public void createUserTest() throws Exception {
        UserRequestDto userRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_user.json").getFile(), UserRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk());

        User expectedUser = userRepository.findByEmail(userRequestDto.getEmail()).get();

        assertEquals(expectedUser.getEmail(), userRequestDto.getEmail());
        assertEquals(expectedUser.getLastName(), userRequestDto.getLastName());
        assertEquals(expectedUser.getFirstName(), userRequestDto.getFirstName());
    }

    @Test
    public void updateUserTest() throws Exception {
        UserRequestDto userRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_update_user.json").getFile(), UserRequestDto.class);
        User expectedUserAfter = userRepository.findByEmail(DEFAULT_EMAIL).get();
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/users/" + expectedUserAfter.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk());

        assertEquals(expectedUserAfter.getEmail(), userRequestDto.getEmail());
        assertEquals(expectedUserAfter.getLastName(), userRequestDto.getLastName());
        assertEquals(expectedUserAfter.getFirstName(), userRequestDto.getFirstName());
    }

    @Test
    public void deleteUserTest() throws Exception {
        User expectedUser = userRepository.findByEmail(DEFAULT_EMAIL).get();
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/users/" + expectedUser.getId()))
                .andExpect(status().isOk());
    }
}
