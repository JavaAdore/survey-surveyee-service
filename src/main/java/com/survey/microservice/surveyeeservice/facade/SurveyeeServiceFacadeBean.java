package com.survey.microservice.surveyeeservice.facade;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.survey.microservice.base.exception.ServiceException;
import com.survey.microservice.surveyeeservice.model.RespondSurveyModel;
import com.survey.microservice.surveyeeservice.model.SurveyBasicInfo;
import com.survey.microservice.surveyeeservice.model.SurveyMetaData;
import com.survey.microservice.surveyeeservice.model.SurveyQuestionMetaData;
import com.survey.microservice.surveyeeservice.service.api.SurveyeeService;
import com.survey.microservice.surveyeeservice.service.api.ValidationService;

@Service
public class SurveyeeServiceFacadeBean implements SurveyeeServiceFacade {

	
	
	private SurveyeeService surveyeeService;
	private ValidationService validationService;
	
	public SurveyeeServiceFacadeBean(SurveyeeService surveyeeService,ValidationService validationService) {
		super();
		this.surveyeeService = surveyeeService;
		this.validationService=validationService;
	}




	// assuming a lot of instances shall created for that service, it will be heavily used by surveyees
	// so it will cache SurveyMetaData metadata in Redis
	@Override
	public SurveyMetaData loadSurveyMetaData(Long surveyId, Long surveyVersion) throws ServiceException {

		return surveyeeService.loadSurveyMetaData(surveyId,surveyVersion);
	}
	
	

	@Override
	public void respondSurvey(Long surveyId, Long surveyVersion, RespondSurveyModel respondSurveyModel)
			throws ServiceException {
		validationService.validateRespondSurvey(surveyId,surveyVersion,respondSurveyModel);
	 
		surveyeeService.respondSurvey(surveyId,surveyVersion,respondSurveyModel);
	}



	@Override
	public SurveyMetaData getSurveyMetaDataWithStatistics(Long surveyId, Long surveyVersion) throws ServiceException {
		SurveyMetaData surveyMetaData = loadSurveyMetaData(  surveyId, surveyVersion);
		surveyeeService.populateSurveyResult(surveyMetaData);
		return surveyMetaData;
	}




	@Override
	public SurveyQuestionMetaData getQuestionMetaDataWithStatistics(Long surveyId, Long surveyVersion,
			Long questionId) throws ServiceException {
		SurveyMetaData surveyMetaData = loadSurveyMetaData(surveyId,surveyVersion);
		validationService.validateQuestionMetaDataWithStatistics(surveyMetaData,questionId);
		SurveyQuestionMetaData surveyQuestionMetaData =surveyMetaData.getSurveyQuestionMetaData(questionId);
		surveyeeService.populateSurveyQuestionMetaDataStatistics(surveyQuestionMetaData);
 		return surveyQuestionMetaData;
	}




	@Transactional
	@Override
	public void deleteAllData() {
		surveyeeService.deleteAllData();
	}




	

	 
}
