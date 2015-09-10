package io.github.scry;

import java.io.File;
import java.net.URI;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@SpringBootApplication
@Configuration
public class ScryOracleApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ScryOracleApplication.class, args);
        context.publishEvent(new ContextReadyEvent(context));
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @ConfigurationProperties(prefix = "oracle")
    @Component
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OracleProperties {

        URI downloadUrl;

        File file;

        List<String> setOrders;

    }

}
