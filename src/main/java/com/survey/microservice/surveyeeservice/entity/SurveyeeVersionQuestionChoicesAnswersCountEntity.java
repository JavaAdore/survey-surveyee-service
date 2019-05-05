package com.survey.microservice.surveyeeservice.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.survey.microservice.surveyeeservice.model.ActionType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * The persistent class for the actions database table.
 * 
 */
@Entity(name=SurveyeeVersionQuestionChoicesAnswersCountEntity.ENTITY_NAME)
@Table(name=SurveyeeVersionQuestionChoicesAnswersCountEntity.TABLE_NAME)
@Setter@Getter 
@EqualsAndHashCode
@ToString
// too long name I know , but probably be descriptive 
public class SurveyeeVersionQuestionChoicesAnswersCountEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	public final static String ENTITY_NAME="SurveyeeVersionQuestionChoicesAnswersCount";
	public final static String TABLE_NAME="SURVEYEE_QUESTIONS_CHOICES_ANSWERS_COUNT";
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator="SURVEYEE_QUESTIONS_ANSWERS_PK_SEQ")
	@SequenceGenerator(name="SURVEYEE_QUESTIONS_ANSWERS_PK_SEQ" , sequenceName="SURVEYEE_QUESTIONS_ANSWERS_PK_SEQ",allocationSize=1)
	private Long id;

	@Column(name="survey_id")
	private Long surveyId;

	@Column(name="SURVEY_VERSION")
	private Long surveyVersion;

	@Column(name="SURVEY_QUSTION_ID")
	private Long surveyQuestionId;
	
	@Column(name="SURVEY_QUSTION_CHOICE_ID")
	private Long surveyQuestionChoiceId;
	
	@Column(name="SURVEY_QUSTION_CHOICE_COUNT")
	private Long surveyQuestionChoiceCount;
	
 	  

	
	
	 

}