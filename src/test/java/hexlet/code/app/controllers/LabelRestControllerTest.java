package hexlet.code.app.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.LabelRequestDto;
import hexlet.code.app.dto.LabelResponseDto;
import hexlet.code.app.dto.LoginRequestDto;
import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
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
public class LabelRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

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
        LabelRequestDto labelRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_default_label.json").getFile(), LabelRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/labels")
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelRequestDto))).andExpect(status().isOk());
    }

    @AfterEach
    public void clearLabel() {
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getOneLabelTest() throws Exception {
        Label label = labelRepository.findAll().get(0);
        // Почему здесь тест проходит без хедера с токеном? А в тестах ниже, падает
        var response = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/labels/" + label.getId()))
                .andExpect(status().isOk()).andReturn().getResponse();

        LabelResponseDto labelResponseDto = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(label.getId(), labelResponseDto.getId());
        assertEquals(label.getName(), labelResponseDto.getName());
        assertEquals(label.getCreatedAt().getTime(), labelResponseDto.getCreatedAt().getTime());
    }

    @Test
    public void getAllLabelTest() throws Exception {
        long size = labelRepository.count();

        var response = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/labels")
                        .header(AUTHORIZATION, token))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<LabelResponseDto> labelResponseDtoList = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(size, labelResponseDtoList.size());
    }

    @Test
    public void createLabelTest() throws Exception {
        LabelRequestDto labelRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_label.json").getFile(), LabelRequestDto.class);
        var response = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/labels")
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelRequestDto))).andExpect(status().isOk())
                .andReturn().getResponse();

        LabelResponseDto labelResponseDto = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(labelRequestDto.getName(), labelResponseDto.getName());
    }

    @Test
    public void updateLabelTest() throws Exception {
        Label label = labelRepository.findAll().get(0);
        LabelRequestDto labelRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_update_label.json").getFile(), LabelRequestDto.class);
        var response = mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/labels/" + label.getId())
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labelRequestDto))).andExpect(status().isOk())
                .andReturn().getResponse();
        Label labelAfterUpdate = labelRepository.getById(label.getId());

        LabelResponseDto labelResponseDto = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(labelAfterUpdate.getName(), labelResponseDto.getName());
    }

    // TODO: 23.11.2022 1.Доделать тесты для всех контроллеров 2. Swagger 3. Эксплуатация 4. Деплой
}
