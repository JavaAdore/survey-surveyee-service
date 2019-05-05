package com.survey.microservice.surveyeeservice.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class SurveyBasicInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Long surveyId;
	
	protected String surveyCode;

	protected Long surveyVersion;
	
	protected String title;
	
	
}
