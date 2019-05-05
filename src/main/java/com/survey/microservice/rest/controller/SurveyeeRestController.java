package com.survey.microservice.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.survey.microservice.base.exception.ServiceException;
import com.survey.microservice.base.model.ServiceResponse;
import com.survey.microservice.base.model.SuccessServiceResponse;
import com.survey.microservice.surveyeeservice.facade.SurveyeeServiceFacade;
import com.survey.microservice.surveyeeservice.model.RespondSurveyModel;
import com.survey.microservice.surveyeeservice.model.SurveyBasicInfo;
import com.survey.microservice.surveyeeservice.model.SurveyMetaData;
import com.survey.microservice.surveyeeservice.model.SurveyQuestionMetaData;

@RestController()
@RequestMapping("/")
public class SurveyeeRestController {

	private SurveyeeServiceFacade surveyeeServiceFacade;

	public SurveyeeRestController(SurveyeeServiceFacade surveyeeServiceFacade) {
		this.surveyeeServiceFacade = surveyeeServiceFacade;
	}

	
	
	
	// graph object contains meta data about the survey with it's questions and
	// answers 
	@RequestMapping(path = "/{surveyId}/{surveyVersion}", method = RequestMethod.GET)
	public ServiceResponse<SurveyMetaData> getSurveyMetaData(@PathVariable("surveyId") Long surveyId,
			@PathVariable("surveyVersion") Long surveyVersion) throws ServiceException {
		
		SurveyMetaData surveyMetaData = surveyeeServiceFacade.loadSurveyMetaData(surveyId, surveyVersion);

		return new SuccessServiceResponse<SurveyMetaData>(surveyMetaData);
	}
 

	@RequestMapping(path = "respond/{surveyId}/{surveyVersion}", method = RequestMethod.POST)
	public ServiceResponse<Object> respondSurvey( @PathVariable("surveyId") Long surveyId, @PathVariable("surveyVersion") Long surveyVersion ,    @RequestBody RespondSurveyModel respondSurveyModel) throws ServiceException
	{
		surveyeeServiceFacade.respondSurvey(  surveyId , surveyVersion, respondSurveyModel);
		return new SuccessServiceResponse<Object>(null);
	}
	
	@RequestMapping(path = "statistics/{surveyId}/{surveyVersion}", method = RequestMethod.GET)
	public ServiceResponse<SurveyMetaData> getSurveyMetaDataWithStatistics(@PathVariable("surveyId") Long surveyId, @PathVariable("surveyVersion") Long surveyVersion ) throws ServiceException
	{
		SurveyMetaData surveyMetaData = surveyeeServiceFacade.getSurveyMetaDataWithStatistics(surveyId,surveyVersion);
		return new SuccessServiceResponse<SurveyMetaData>(surveyMetaData);
	}
	
 	@RequestMapping(path = "statistics/{surveyId}/{surveyVersion}/{questionId}", method = RequestMethod.GET)
	public ServiceResponse<SurveyQuestionMetaData> getQuestionMetaDataWithStatistics(@PathVariable("surveyId") Long surveyId, @PathVariable("surveyVersion") Long surveyVersion ,@PathVariable("questionId") Long questionId ) throws ServiceException
	{
 		SurveyQuestionMetaData surveyQuestionMetaData =surveyeeServiceFacade.getQuestionMetaDataWithStatistics(surveyId , surveyVersion ,questionId );
 		return new SuccessServiceResponse<SurveyQuestionMetaData>(surveyQuestionMetaData);
	}
 	
 	@RequestMapping(path = "delete-all-data", method = RequestMethod.DELETE)
 	public ServiceResponse<Object> reset()
 	{
 		surveyeeServiceFacade.deleteAllData();
 		return new SuccessServiceResponse<Object>(null);
 	}
	

}
