package com.survey.microservice.surveyeeservice.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class SurveyQuestionMetaData  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String title;
	
	private String code;
	
	private Long totalNumberOfAllAnswers;
	
	private List<SurveyQuestionChoiceMetaData> surveyQuestionChoiceMetaDataList;
	
	@JsonIgnore
	private Map<Long,SurveyQuestionChoiceMetaData> surveyQuestionChoiceMetaDataMap = new HashMap<Long,SurveyQuestionChoiceMetaData>();

	public void populateCachingMap() {
		if(null != surveyQuestionChoiceMetaDataList)
		{
			surveyQuestionChoiceMetaDataList.parallelStream().forEach(c->
			{
				surveyQuestionChoiceMetaDataMap.put(c.getId(), c);
			});
		}
	}
	
	public boolean hasChoice(Long choiceId)
	{
		return surveyQuestionChoiceMetaDataMap.containsKey(choiceId);
	}

	public void assignQuestionChoicesStatistics() {
		// get the sum for choice count for all of them
		// thanks java 8.
		Optional<Long> totalChoicesCountOptional=   surveyQuestionChoiceMetaDataList.stream().map(sqcmd->sqcmd.getNumberOfAnswers()).reduce((a,b)->a+b);
		

		if(totalChoicesCountOptional.isPresent())
		{
			Long totalNumberOfallAnswers = totalChoicesCountOptional.get();
			this.totalNumberOfAllAnswers=totalNumberOfallAnswers;
			// no need to check value equal zero ... as per design .. it never happens
			surveyQuestionChoiceMetaDataList.parallelStream().forEach(c -> c.calculatePersentage(totalNumberOfallAnswers));

		}
		
	}
	
	
	public SurveyQuestionChoiceMetaData getSurveyQuestionChoiceMetaData(Long choiceId)
	{
		return surveyQuestionChoiceMetaDataMap.get(choiceId);
	}
	
	 
}
