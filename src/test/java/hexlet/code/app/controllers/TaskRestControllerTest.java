package hexlet.code.app.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.*;
import hexlet.code.app.model.Task;
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
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static hexlet.code.app.utils.TestUtils.baseUrl;
import static hexlet.code.app.utils.TestUtils.fromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
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
    void getOneTaskTest() throws Exception {
        Task task = taskRepository.findAll().get(0);

        var response = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/tasks/" + task.getId()))
                .andExpect(status().isOk()).andReturn().getResponse();

        TaskResponseDto taskResponseDto = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(task.getId(), taskResponseDto.getId());
        assertEquals(task.getName(), taskResponseDto.getName());
        assertEquals(task.getDescription(), taskResponseDto.getDescription());
        assertEquals(task.getTaskStatus().getId(), taskResponseDto.getTaskStatus().getId());
        assertEquals(task.getAuthor().getId(), taskResponseDto.getAuthor().getId());
        assertEquals(task.getExecutor().getId(), taskResponseDto.getExecutor().getId());
        assertEquals(task.getCreatedAt().getTime(), taskResponseDto.getCreatedAt().getTime());
    }

    @Test
    void getAllTaskTest() throws Exception {
        long size = taskRepository.count();

        var response = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/tasks")
                        .header(AUTHORIZATION, token))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<TaskResponseDto> taskResponseDtoList = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(size, taskResponseDtoList.size());
    }

    @Test
    void createTaskTest() throws Exception {
        TaskRequestDto taskRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_task.json").getFile(), TaskRequestDto.class);
        var response = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/tasks")
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDto))).andExpect(status().isOk())
                .andReturn().getResponse();

        TaskResponseDto taskResponseDto = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(taskRequestDto.getName(), taskResponseDto.getName());
        assertEquals(taskRequestDto.getDescription(), taskResponseDto.getDescription());
        assertEquals(taskRequestDto.getTaskStatusId(), taskResponseDto.getTaskStatus().getId());
        assertEquals(taskRequestDto.getAuthorId(), taskResponseDto.getAuthor().getId());
        assertEquals(taskRequestDto.getExecutorId(), taskResponseDto.getExecutor().getId());
    }

    // TODO: 15.01.2023 Доделать остальные тесты
}
