package com.example.progettoSSD.service;

import java.util.List;

import com.example.progettoSSD.entity.CompletedHomeworks;
import com.example.progettoSSD.entity.Homework;


public interface HomeworkService {

	Homework saveHomework(Homework Homework);
	CompletedHomeworks saveComplHomeworks(CompletedHomeworks CompletedHomeworks);
	List<CompletedHomeworks> getCompletedHomeworks();
	List<Homework> getAllHomework();
	Homework getHomeworkById(String id);
	Homework updateHomework(Homework Homework,String id);
	void deleteHomeworkByName(String nome);
	
}