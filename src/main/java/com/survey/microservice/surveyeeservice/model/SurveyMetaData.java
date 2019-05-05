package com.survey.microservice.surveyeeservice.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter@Getter@ToString

public class SurveyMetaData extends SurveyBasicInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	private List<SurveyQuestionMetaData> surveyQuestionMetaDataList;
	
	@JsonIgnore
	private Map<Long,SurveyQuestionMetaData> surveyQuestionMetaDataMap;
	
	
	public void populateCachingMap()
	{
		if(null !=surveyQuestionMetaDataList)
		{
			
			surveyQuestionMetaDataMap = new HashMap<>();
			surveyQuestionMetaDataList.parallelStream().forEach(q->
			{
				q.populateCachingMap();
				
				surveyQuestionMetaDataMap.put(q.getId(), q);
			});
		}
	}
	
	public boolean hasQuestion(Long questionId)
	{
		return surveyQuestionMetaDataMap.containsKey(questionId);
	}
	
	public boolean isValidChoice(Long questionId , Long choiceId)
	{
		return surveyQuestionMetaDataMap.get(questionId).hasChoice(choiceId);
	}

	public void assignQuestionChoiceCount(Long questionId, Long choiceId, Long choiceCount) {
		// as per design .. both values could not come with null .. so no need to check
		SurveyQuestionMetaData surveyQuestionMetaData =	getSurveyQuestionMetaData(questionId);
		
		SurveyQuestionChoiceMetaData surveyQuestionChoiceMetaData = surveyQuestionMetaData.getSurveyQuestionChoiceMetaData(choiceId);
		 
		surveyQuestionChoiceMetaData.setNumberOfAnswers(choiceCount);
	}

	public void assignQuestionChoicesStatistics(Long questionId) {
  
		SurveyQuestionMetaData surveyQuestionMetaData =getSurveyQuestionMetaData(questionId);
		// it never comes with null so no need to check
		surveyQuestionMetaData.assignQuestionChoicesStatistics();
	}
	
	public SurveyQuestionMetaData getSurveyQuestionMetaData(Long questionId)
	{
		return surveyQuestionMetaDataMap.get(questionId);
	}
	
}
