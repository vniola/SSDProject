package com.example.progettoSSD.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoVaultConfig {

    @Value("${spring.cloud.vault.database.username}")
    private String username;

    @Value("${spring.cloud.vault.database.password}")
    private String password;

    @Bean
    public MongoClient mongoClient() {
        // Aggiungi il nome del database all'URI di connessione MongoDB
        String uriWithDatabase = "mongodb://" + username + ":" + password + "@192.168.64.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&authSource=keycloak&appName=mongosh+2.1.1";
        
        return MongoClients.create(uriWithDatabase);
    }
}