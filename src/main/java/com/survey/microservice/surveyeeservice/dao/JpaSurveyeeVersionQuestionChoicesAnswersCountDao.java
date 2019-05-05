package com.survey.microservice.surveyeeservice.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.survey.microservice.surveyeeservice.entity.SurveyeeVersionQuestionChoicesAnswersCountEntity;

@Repository
public interface JpaSurveyeeVersionQuestionChoicesAnswersCountDao extends JpaRepository<SurveyeeVersionQuestionChoicesAnswersCountEntity, Long> {

	// could select just be property convention .. but I prefer using @Query()
	@Query("select svqcac from "+SurveyeeVersionQuestionChoicesAnswersCountEntity.ENTITY_NAME + " svqcac where svqcac.surveyQuestionChoiceId =:#{#choiceId} ")
	Optional<SurveyeeVersionQuestionChoicesAnswersCountEntity> findByChoiceId(@Param("choiceId") Long choiceId);

	@Modifying
	@Query("update "+SurveyeeVersionQuestionChoicesAnswersCountEntity.ENTITY_NAME + " set surveyQuestionChoiceCount = surveyQuestionChoiceCount+1  where surveyQuestionChoiceId =:#{#choiceId} ")
	void increaseChoiceCount(@Param("choiceId") Long choiceId);

	@Query("select svqcace from "+SurveyeeVersionQuestionChoicesAnswersCountEntity.ENTITY_NAME + " svqcace where svqcace.surveyId =:#{#surveyId}   and svqcace.surveyVersion =:#{#surveyVersion}   order by svqcace.surveyQuestionId  ")
	List<SurveyeeVersionQuestionChoicesAnswersCountEntity> getSurveyRecords(@Param("surveyId") Long surveyId , @Param("surveyVersion") Long surveyVersion);

	@Query("select svqcace from "+SurveyeeVersionQuestionChoicesAnswersCountEntity.ENTITY_NAME + " svqcace where svqcace.surveyQuestionId =:#{#questionId}   ")
	List<SurveyeeVersionQuestionChoicesAnswersCountEntity> getSurveyQuestionRecords(Long questionId);

}
