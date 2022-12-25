package hexlet.code.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    public static final String DEFAULT_EMAIL = "ivan2@google.com";

    public static final String baseUrl = "/api";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String asJson(final Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return mapper.readValue(json, to);
    }
}
