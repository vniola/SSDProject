package com.example.progettoSSD.repository;
import com.example.progettoSSD.entity.*;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface HomeworkRepository extends MongoRepository<Homework, String> {
    Homework findByNome(String nome);
}