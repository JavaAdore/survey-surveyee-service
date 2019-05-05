package com.survey.microservice.base.feignclient;

import org.springframework.stereotype.Component;

import com.survey.microservice.surveyeeservice.model.SurveyMetaData;

@Component
public class SurveyDefinitionFeignClientFallback implements SurveyDefinitionFeignClient {

	@Override
	public SurveyMetaData getSurveyMetaData(Long surveyId, Long surveyVersion)  {
		 
		return null;
	}

}
