package com.akulinski.r8meservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;

import java.io.IOException;
import java.util.Map;

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.nodes}")
    private String hostAndPort;

    @Override
    public EntityMapper entityMapper() {
        return new CustomEntityMapper();
    }


    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
            .connectedTo(hostAndPort)
            .build();

        return RestClients.create(clientConfiguration).rest();
    }

    public static class CustomEntityMapper implements EntityMapper {

        private final ObjectMapper objectMapper;

        public CustomEntityMapper() {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objectMapper.registerModule(new CustomGeoModule());
            objectMapper.registerModule(new JavaTimeModule());
        }

        @Override
        public String mapToString(Object object) throws IOException {
            return objectMapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }

        @Override
        public Map<String, Object> mapObject(Object o) {
            return null;
        }

        @Override
        public <T> T readObject(Map<String, Object> map, Class<T> aClass) {
            return null;
        }
    }
}
