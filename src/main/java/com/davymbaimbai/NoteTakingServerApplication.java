package com.davymbaimbai;

import com.davymbaimbai.config.ZonedDateTimeDeserializer;
import com.davymbaimbai.config.ZonedDateTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.ZonedDateTime;

@SpringBootApplication
public class NoteTakingServerApplication {
	private static final String THIRD_PARTY_SERVER_URL = "https://notes-taking-server.free.beeceptor.com";
    public static void main(String[] args) {
        SpringApplication.run(NoteTakingServerApplication.class, args);
    }
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(THIRD_PARTY_SERVER_URL)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}
