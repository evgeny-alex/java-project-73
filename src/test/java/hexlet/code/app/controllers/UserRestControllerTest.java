package hexlet.code.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.UserRequestDto;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void getOneUserTest() throws Exception {
        String userId = "1";
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"json/request_create_user.json"})
    public void createUserTest(String resourcePathData) throws Exception {
        UserRequestDto userRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:" + resourcePathData).getFile(), UserRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"json/request_update_user.json"})
    public void updateUserTest(String resourcePathData) throws Exception {
        UserRequestDto userRequestDto = objectMapper.readValue(resourceLoader.getResource("classpath:" + resourcePathData).getFile(), UserRequestDto.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserTest() throws Exception {
        String userId = "1";
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userId))
                .andExpect(status().isOk());
    }
}
