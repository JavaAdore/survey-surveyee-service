package com.survey.microservice.surveyeeservice.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import lombok.Data;

@Data
public class SurveyQuestionChoiceMetaData  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String title;

	private long numberOfAnswers;
	
	private BigDecimal persentage ;
	
	
	
	public void calculatePersentage(Long totalChoicesCount)
	{ 
		
		double persentage =  ((double)numberOfAnswers/totalChoicesCount)*100;
		if(persentage%100!=0)
		{
			this.persentage= new BigDecimal(persentage).setScale(2, RoundingMode.HALF_UP);
		}else
		{
			this.persentage = new BigDecimal(persentage);
		}
		 
	}
	
	// I use lombok for creating setters and getters ... but in that case ..
	// I implement the getter method my self .. to return default value  BigDecimal.ZERO
	// this will happend only if surveyee choosed that choice
	
	public BigDecimal getPersentage()
	{
		if(null == persentage)
		{
			return BigDecimal.ZERO;
		}
		return persentage;
	} 
 
	
	
 }
