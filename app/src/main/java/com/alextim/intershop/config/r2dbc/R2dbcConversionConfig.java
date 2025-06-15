package com.alextim.intershop.config.r2dbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.Arrays;
import java.util.List;

@Configuration
public class R2dbcConversionConfig {

    @Bean
    public R2dbcCustomConversions customConversions() {
        List<Object> converters = Arrays.asList(
                new ListToJsonConverter(),
                new JsonToListConverter()
        );
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }

    @WritingConverter
    public class ListToJsonConverter implements Converter<List<String>, String> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String convert(List<String> source) {
            try {
                return objectMapper.writeValueAsString(source);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error serializing list to JSON", e);
            }
        }
    }

    @ReadingConverter
    public class JsonToListConverter implements Converter<String, List<String>> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public List<String> convert(String source) {
            try {
                return objectMapper.readValue(source, List.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error deserializing JSON to list", e);
            }
        }
    }
}
