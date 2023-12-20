package com.example.progettoSSD.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collection = "Homeworks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Homework {
	
	@Id
	@NotNull(message = "Homework id should not be null or empty")
	@Pattern(message = "Alert", regexp = "[a-zA-Z0-9 ]+")
	@Size(max=30)
	private String id;

	@NotBlank(message = "Homework name should not be null or empty")
	@Pattern(message = "Alert", regexp = "[a-zA-Z0-9 ]+")
	@Size(max=30)
	private String nome;

	@NotBlank(message = "Homework deadline should be in the following format: dd/mm/yy")
	@Pattern(message = "Formato data non valido. Utilizzare dd/mm/yy", regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{2}$")
	@Size(min = 8, max = 8, message = "Deadline should be of 8 digits")
	private String deadline;

	@NotBlank(message = "Complete should be true or false")
	private boolean complete;

	@NotNull
	private String traccia;
}