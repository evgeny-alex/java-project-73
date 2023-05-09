package hexlet.code.app.config;

import com.rollbar.notifier.Rollbar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rollbar.spring.webmvc.RollbarSpringConfigBuilder.withAccessToken;

@Configuration
public class RollbarConfig {

//    @Value("${ROLLBAR_TOKEN}")
    private String rollbarToken;

    @Bean
    public Rollbar rollbar() {
        return Rollbar.init(withAccessToken(rollbarToken)
                .environment("production")
                .codeVersion("1.0.0")
                .build());
    }
}
