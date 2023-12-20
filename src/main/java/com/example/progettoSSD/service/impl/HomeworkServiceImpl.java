package com.example.progettoSSD.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.progettoSSD.exception.ResourceNotFoundException;
import com.example.progettoSSD.entity.*;
import com.example.progettoSSD.repository.*;
import com.example.progettoSSD.service.*;

@Service
public class HomeworkServiceImpl implements HomeworkService {

	@Autowired
	private HomeworkRepository HomeworkRepository;

	@Autowired
	private CompletedHomeworksRepository CompletedHomeworksRepository;

	@Override
	public Homework saveHomework(Homework Homework) {
		return HomeworkRepository.save(Homework);
	}

	@Override
	public CompletedHomeworks saveComplHomeworks(CompletedHomeworks CompletedHomeworks) {
		return CompletedHomeworksRepository.save(CompletedHomeworks);
	}

	@Override
	public List<Homework> getAllHomework() {
		return HomeworkRepository.findAll();
	}

	@Override
	public List<CompletedHomeworks> getCompletedHomeworks() {
		return CompletedHomeworksRepository.findAll();
	}

	@Override
	public Homework getHomeworkById(String id) {
		Optional<Homework> Homework = HomeworkRepository.findById(id);
		if (Homework.isPresent()) {
			return Homework.get();
		}
		throw new ResourceNotFoundException("Expense is not found for the id "+id);
	}

	@Override
	public Homework updateHomework(Homework Homework, String id) {
		Homework existingHomework = getHomeworkById(id);
		existingHomework.setNome(Homework.getNome() != null ? Homework.getNome() : existingHomework.getNome());
		existingHomework.setDeadline(Homework.getDeadline() != null ? Homework.getDeadline() : existingHomework.getDeadline());
		//existingHomework.setComplete(Homework.getComplete() != null ? Homework.getComplete() : existingHomework.getComplete());
		return HomeworkRepository.save(existingHomework);
	}

	@Override
	public void deleteHomeworkByName(String nome) {
		Homework homeworkToDelete = HomeworkRepository.findByNome(nome);
		if (homeworkToDelete != null) {
			HomeworkRepository.delete(homeworkToDelete);
		} else {
			throw new ResourceNotFoundException("Homework non trovato per il nome " + nome);
		}
	}
	
}
