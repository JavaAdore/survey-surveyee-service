package com.survey.microservice.base.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.survey.microservice.base.exception.ServiceException;
import com.survey.microservice.base.feignclient.config.ContentExtractorFeignConfiuration;
import com.survey.microservice.surveyeeservice.model.SurveyMetaData;

@FeignClient(name = "SURVEY-DEFINITION-SERVICE", configuration = ContentExtractorFeignConfiuration.class ,
fallback = SurveyDefinitionFeignClientFallback.class)
// fall back will called when failure to call api 
public interface SurveyDefinitionFeignClient {
	
	@RequestMapping(path = "survey/metadata/{surveyId}/{surveyVersion}", method = RequestMethod.GET)
	public SurveyMetaData getSurveyMetaData(@PathVariable("surveyId") Long surveyId, @PathVariable("surveyVersion") Long surveyVersion) ;

	 
}
