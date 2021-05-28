package com.grebenkov;

import com.grebenkov.extensions.FileExtension;
import com.grebenkov.parser.OrderParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.EnumMap;
import java.util.Map;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public Map<FileExtension, OrderParser> getParsers(
            @Qualifier("jsonObjectParser") OrderParser jsonObjectParser,
            @Qualifier("csvObjectParser") OrderParser csvObjectParser
    ) {
        final Map<FileExtension, OrderParser> parsers = new EnumMap<>(FileExtension.class);
        parsers.put(FileExtension.CSV, csvObjectParser);
        parsers.put(FileExtension.JSON, jsonObjectParser);
        return parsers;
    }
}
