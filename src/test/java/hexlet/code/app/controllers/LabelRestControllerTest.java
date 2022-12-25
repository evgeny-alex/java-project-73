package hexlet.code.app.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.LabelRequestDto;
import hexlet.code.app.dto.LabelResponseDto;
import hexlet.code.app.dto.LoginRequestDto;
import hexlet.code.app.dto.UserRequestDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
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

import java.nio.charset.StandardCharsets;

import static hexlet.code.app.utils.TestUtils.baseUrl;
import static hexlet.code.app.utils.TestUtils.fromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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
        String token = response.getContentAsString();

        // Создание лейбла
        // TODO: 24.12.2022 Разобраться с передачей токена в хедере 
        LabelRequestDto labelRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:json/request_create_default_label.json").getFile(), LabelRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/labels")
                .header(AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelRequestDto))).andExpect(status().isOk());
    }

    @AfterEach
    public void clearLabel() {
        labelRepository.deleteAll();
    }

    @Test
    public void getOneLabelTest() throws Exception {
        Label label = labelRepository.findAll().get(0);
        var response = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/labels/" + label.getId()))
                .andExpect(status().isOk()).andReturn().getResponse();

        LabelResponseDto labelResponseDto = fromJson(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertEquals(label.getId(), labelResponseDto.getId());
        assertEquals(label.getName(), labelResponseDto.getName());
        assertEquals(label.getCreatedAt().getTime(), labelResponseDto.getCreatedAt().getTime());
    }

    // TODO: 23.11.2022 1.Доделать тесты для всех контроллеров 2. Swagger 3. Эксплуатация 4. Деплой
}
