package com.survey.microservice.surveyeeservice.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class QuestionAnswer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long questionId;
	private Long choiceId;
	
	
	
}
