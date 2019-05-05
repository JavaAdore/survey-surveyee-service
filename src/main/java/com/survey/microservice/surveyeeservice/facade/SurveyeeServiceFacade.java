package com.survey.microservice.surveyeeservice.facade;

import java.util.List;

import com.survey.microservice.base.exception.ServiceException;
import com.survey.microservice.surveyeeservice.model.RespondSurveyModel;
import com.survey.microservice.surveyeeservice.model.SurveyBasicInfo;
import com.survey.microservice.surveyeeservice.model.SurveyMetaData;
import com.survey.microservice.surveyeeservice.model.SurveyQuestionMetaData;

public interface SurveyeeServiceFacade {

	SurveyMetaData loadSurveyMetaData(Long surveyId, Long surveyVersion) throws ServiceException;

	void respondSurvey(Long surveyId, Long surveyVersion, RespondSurveyModel respondSurveyModel) throws ServiceException;

	SurveyMetaData getSurveyMetaDataWithStatistics(Long surveyId, Long surveyVersion) throws ServiceException;

	SurveyQuestionMetaData getQuestionMetaDataWithStatistics(Long surveyId, Long surveyVersion, Long questionId) throws ServiceException;

	void deleteAllData();


 
	 

  
	 
}
