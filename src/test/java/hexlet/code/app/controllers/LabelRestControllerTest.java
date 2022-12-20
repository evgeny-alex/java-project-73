package hexlet.code.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

    @Test
    public void getOneLabelTest() throws Exception {
        String labelId = "1";
        mockMvc.perform(MockMvcRequestBuilders.get("/labels/" + labelId))
                .andExpect(status().isOk());
    }

    // TODO: 23.11.2022 1.Доделать тесты для всех контроллеров, замокать все обращения и проверить 2. Swagger 3. Эксплуатация 4. Деплой
}
