package com.survey.microservice.surveyeeservice.service.api;

import com.survey.microservice.base.exception.ServiceException;
import com.survey.microservice.surveyeeservice.model.RespondSurveyModel;
import com.survey.microservice.surveyeeservice.model.SurveyMetaData;

public interface ValidationService {

	void validateRespondSurvey(Long surveyId, Long surveyVersion, RespondSurveyModel respondSurveyModel) throws ServiceException;

	void validateQuestionMetaDataWithStatistics(SurveyMetaData surveyMetaData, Long questionId) throws ServiceException;

	 

  

	 
}
