package hexlet.code.app.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.*;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.TaskStatus;
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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TaskStatusRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    private String token;

    @BeforeEach
    public void initLabel() throws Exception {
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
        TaskStatusRequestDto taskStatusRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_default_task_status.json").getFile(), TaskStatusRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/statuses")
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskStatusRequestDto))).andExpect(status().isOk());
    }

    @AfterEach
    public void clearLabel() {
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
    }

    @Test
    public void getOneTaskStatusTest() throws Exception {
        TaskStatus taskStatus = taskStatusRepository.findAll().get(0);
        var response = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/statuses/" + taskStatus.getId()))
                .andExpect(status().isOk()).andReturn().getResponse();

        TaskStatusResponseDto taskStatusResponseDto = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(taskStatus.getId(), taskStatusResponseDto.getId());
        assertEquals(taskStatus.getName(), taskStatusResponseDto.getName());
        assertEquals(taskStatus.getCreatedAt().getTime(), taskStatusResponseDto.getCreatedAt().getTime());
    }

    @Test
    public void getAllTaskStatusTest() throws Exception {
        long size = taskStatusRepository.count();
        var response = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/statuses")
                        .header(AUTHORIZATION, token))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<TaskStatusResponseDto> taskStatusResponseDtoList = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(size, taskStatusResponseDtoList.size());
    }

    @Test
    public void createTaskStatusTest() throws Exception {
        TaskStatusRequestDto taskStatusRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_task_status.json").getFile(), TaskStatusRequestDto.class);
        var response = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/statuses")
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusRequestDto))).andExpect(status().isOk())
                .andReturn().getResponse();

        TaskStatusResponseDto taskStatusResponseDto = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(taskStatusRequestDto.getName(), taskStatusResponseDto.getName());
    }

    @Test
    public void updateTaskStatusTest() throws Exception {
        TaskStatus taskStatus = taskStatusRepository.findAll().get(0);
        TaskStatusRequestDto TaskStatusRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_update_task_status.json").getFile(), TaskStatusRequestDto.class);
        var response = mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/statuses/" + taskStatus.getId())
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TaskStatusRequestDto))).andExpect(status().isOk())
                .andReturn().getResponse();
        TaskStatus taskStatusAfterUpdate = taskStatusRepository.getById(taskStatus.getId());

        TaskStatusResponseDto taskStatusResponseDto = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(taskStatusAfterUpdate.getName(), taskStatusResponseDto.getName());
    }

    @Test
    public void deleteTaskStatusTest() throws Exception {
        TaskStatus taskStatus = taskStatusRepository.findAll().get(0);
        int countTaskStatusBeforeDelete = taskStatusRepository.findAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/statuses/" + taskStatus.getId())
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        int countTaskStatusAfterDelete = taskStatusRepository.findAll().size();

        assertEquals(countTaskStatusBeforeDelete - 1, countTaskStatusAfterDelete);

    }

}
