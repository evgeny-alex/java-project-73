package hexlet.code.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.dto.UserResponseDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return mapper.readValue(json, to);
    }

    private static final String baseUrl = "/api";

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

        User expectedUser = userRepository.getById(Integer.parseInt(userRequestDto.getId()));

        assertEquals(expectedUser.getEmail(), userRequestDto.getEmail());
        assertEquals(expectedUser.getLastName(), userRequestDto.getLastName());
        assertEquals(expectedUser.getFirstName(), userRequestDto.getFirstName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"json/request_update_user.json"})
    public void updateUserTest(String resourcePathData) throws Exception {
        UserRequestDto userRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:" + resourcePathData).getFile(), UserRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserTest() throws Exception {
        String userId = "1";
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/users/" + userId))
                .andExpect(status().isOk());
    }
}
