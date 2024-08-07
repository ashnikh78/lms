package com.lms.usermanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class MongoConfig {

    private final Dotenv dotenv = Dotenv.load();

    @Bean
    public MongoClient mongoClient() {
        String mongoUri = dotenv.get("SPRING_DATA_MONGODB_URI");
        return MongoClients.create(mongoUri);
    }
}