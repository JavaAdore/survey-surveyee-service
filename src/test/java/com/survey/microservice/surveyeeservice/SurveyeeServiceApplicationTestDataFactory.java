package com.survey.microservice.surveyeeservice;

import static com.survey.microservice.surveyeeservice.SurveyeeServiceApplicationTestDataFactory.ANSWER_ID;
import static com.survey.microservice.surveyeeservice.SurveyeeServiceApplicationTestDataFactory.QUESTION_ID;

import com.survey.microservice.surveyeeservice.model.SurveyQuestionChoiceMetaData;

public class SurveyeeServiceApplicationTestDataFactory {

	protected static Long QUESTION_ID;
	protected static Long ANSWER_ID;
	
	protected void resetIds() {
		QUESTION_ID = 0l;
		ANSWER_ID = 0l;
	}
	
	protected SurveyQuestionChoiceMetaData createNoAnswer() {
		SurveyQuestionChoiceMetaData surveyQuestionChoiceMetaData = new SurveyQuestionChoiceMetaData();
		surveyQuestionChoiceMetaData.setId(++ANSWER_ID);
		surveyQuestionChoiceMetaData.setTitle("Yes");
		return surveyQuestionChoiceMetaData;
	}

	protected SurveyQuestionChoiceMetaData createYesAnswer() {
		SurveyQuestionChoiceMetaData surveyQuestionChoiceMetaData = createSampleAnswer();
		surveyQuestionChoiceMetaData.setTitle("Yes");
		return surveyQuestionChoiceMetaData;
	}

	protected SurveyQuestionChoiceMetaData createSampleAnswer() {
		SurveyQuestionChoiceMetaData surveyQuestionChoiceMetaData = new SurveyQuestionChoiceMetaData();
		surveyQuestionChoiceMetaData.setId(++ANSWER_ID);
		return surveyQuestionChoiceMetaData;
	}

}
