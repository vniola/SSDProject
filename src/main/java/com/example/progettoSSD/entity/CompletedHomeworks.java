package com.example.progettoSSD.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CompletedHomeworks")
public class CompletedHomeworks {

    @Id
    private String userId;
    private String homeworkId;
    private String content;

    // Costruttore, getters e setters

    public CompletedHomeworks(String userId, String homeworkId, String content) {
        this.userId = userId;
        this.homeworkId = homeworkId;
        this.content = content;
    }

    // Getter e setter per l'ID dell'utente
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter e setter per il nome del compito
    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    // Getter e setter per il contenuto dell'editor
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
