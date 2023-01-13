package hexlet.code.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.*;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
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

import static hexlet.code.app.utils.TestUtils.baseUrl;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    private String token;

    @BeforeEach
    public void initData() throws Exception {
        // Создание пользователя
        UserRequestDto userRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_default_user.json").getFile(), UserRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)));
        // Получение токена
        LoginRequestDto loginRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_login_default.json").getFile(), LoginRequestDto.class);
        var response = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto))).andReturn().getResponse();
        token = "Bearer " + response.getContentAsString();

        // Создание лейбла
        LabelRequestDto labelRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_default_label.json").getFile(), LabelRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/labels")
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelRequestDto))).andExpect(status().isOk());

        // Создание статуса
        TaskStatusRequestDto taskStatusRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_default_task_status.json").getFile(), TaskStatusRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/statuses")
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskStatusRequestDto))).andExpect(status().isOk());

        // Создание задачи
        TaskRequestDto taskRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_default_task.json").getFile(), TaskRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/tasks")
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequestDto))).andExpect(status().isOk());

    }

    @AfterEach
    public void clearData() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getOneTaskTest() {
        // TODO: 13.01.2023 Доделать тест 
    }
}
