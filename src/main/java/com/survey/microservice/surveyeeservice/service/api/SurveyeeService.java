package com.survey.microservice.surveyeeservice.service.api;

import com.survey.microservice.base.exception.ServiceException;
import com.survey.microservice.surveyeeservice.model.RespondSurveyModel;
import com.survey.microservice.surveyeeservice.model.SurveyMetaData;
import com.survey.microservice.surveyeeservice.model.SurveyQuestionMetaData;

public interface SurveyeeService {

	SurveyMetaData loadSurveyMetaData(Long surveyId, Long surveyVersion) throws ServiceException;

	void respondSurvey(Long surveyId, Long surveyVersion, RespondSurveyModel respondSurveyModel);

	void populateSurveyResult(SurveyMetaData surveyMetaData);

	SurveyQuestionMetaData populateSurveyQuestionMetaDataStatistics(SurveyQuestionMetaData surveyQuestionMetaData);

	void deleteAllData();
 

 
}
