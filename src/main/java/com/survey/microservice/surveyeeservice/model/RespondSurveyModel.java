package com.survey.microservice.surveyeeservice.model;

import java.io.Serializable;
import java.util.Set;

import lombok.Data;

@Data
public class RespondSurveyModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// to prevent duplication
	// equal method of QuestionAnswer is implemented by @Data
	private Set<QuestionAnswer> questionAnswers;
}
