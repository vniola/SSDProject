package com.example.progettoSSD.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.progettoSSD.entity.*;

public interface CompletedHomeworksRepository extends MongoRepository<CompletedHomeworks, String> {
    // Metodi personalizzati, se necessario
}