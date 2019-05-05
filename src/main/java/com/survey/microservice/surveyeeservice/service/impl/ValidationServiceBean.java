package com.survey.microservice.surveyeeservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.survey.microservice.base.exception.ServiceException;
import com.survey.microservice.base.model.ErrorMessageCode;
import com.survey.microservice.surveyeeservice.model.QuestionAnswer;
import com.survey.microservice.surveyeeservice.model.RespondSurveyModel;
import com.survey.microservice.surveyeeservice.model.SurveyMetaData;
import com.survey.microservice.surveyeeservice.service.api.SurveyeeService;
import com.survey.microservice.surveyeeservice.service.api.ValidationService;

@Service
public class ValidationServiceBean implements ValidationService {

	private final SurveyeeService surveyeeService;

	public ValidationServiceBean(SurveyeeService surveyeeService) {
		super();
		this.surveyeeService = surveyeeService;
	}

	@Override
	public void validateRespondSurvey(Long surveyId, Long surveyVersion, RespondSurveyModel respondSurveyModel)
			throws ServiceException {
		// if no such survey definition localized service exception shall be thrown
		SurveyMetaData surveyMetaData = surveyeeService.loadSurveyMetaData(surveyId, surveyVersion);

		validateSurveyeeAnsweredAllSurveyQuestions(surveyMetaData, respondSurveyModel);

		validateSurveyQuestionAnswers(surveyMetaData, respondSurveyModel);

	}

	private void validateSurveyQuestionAnswers(SurveyMetaData surveyMetaData, RespondSurveyModel respondSurveyModel)
			throws ServiceException {
		// validate all provided questions are associated with the surveyMetaData
		// validate all provided choices are associated with provided questions
		
		List<Long> wrongQuestionsIds = new ArrayList<>();
		List<QuestionAnswer> wrongAnswers = new ArrayList<>();
		for (QuestionAnswer questionAnswer : respondSurveyModel.getQuestionAnswers()) {
			Long questionId = questionAnswer.getQuestionId();
			Long choiceId = questionAnswer.getChoiceId();
			if (!surveyMetaData.hasQuestion(questionId)) {
				wrongQuestionsIds.add(questionId);
			}

			if (wrongQuestionsIds.isEmpty()) {
				if (!surveyMetaData.isValidChoice(questionId, choiceId)) {
					wrongAnswers.add(questionAnswer);
				}
			}

		}

		// if their is a questions does not belong to the survey meta data 
		// throw business exception wrapping ids of these questions
		if (!wrongQuestionsIds.isEmpty()) {
			throw new ServiceException(ErrorMessageCode.PROVIDED_QUESTIONS_NOT_ASSOCIATED_WITH_PROVIDED_SURVEY,
					wrongQuestionsIds, null);
		}

		// if their are choices associated with wrong questions
		// throw business exception wrappging these choices for front end to handle
		if (!wrongAnswers.isEmpty()) {
			throw new ServiceException(ErrorMessageCode.PROVIDED_ANSWERS_ARE_NOT_ASSOCIATED_WITH_PROVIDED_QUESTION,
					wrongAnswers, null);

		}
	}

	private void validateSurveyeeAnsweredAllSurveyQuestions(SurveyMetaData surveyMetaData,
			RespondSurveyModel respondSurveyModel) throws ServiceException {
		if (surveyMetaData.getSurveyQuestionMetaDataList().size() > respondSurveyModel.getQuestionAnswers().size()) {
			throw new ServiceException(ErrorMessageCode.NOT_ALL_SURVEY_QUESTIONS_HAS_BEEN_ANSWERED,
					getUnAnsweredQuestion(surveyMetaData, respondSurveyModel));
		}
	}

	private Object[] getUnAnsweredQuestion(SurveyMetaData surveyMetaData, RespondSurveyModel respondSurveyModel) {

		Set<Long> allQuestionsIds = surveyMetaData.getSurveyQuestionMetaDataMap().keySet();

		Set<Long> coveredIds = respondSurveyModel.getQuestionAnswers().stream().map(QuestionAnswer::getQuestionId)
				.collect(Collectors.toSet());

		List<Long> uncoveredQuestions = allQuestionsIds.stream().filter(q -> !coveredIds.contains(q))
				.collect(Collectors.toList());
		return uncoveredQuestions.toArray();
	}

	@Override
	public void validateQuestionMetaDataWithStatistics(SurveyMetaData surveyMetaData, Long questionId)
			throws ServiceException {
		if(!surveyMetaData.hasQuestion(questionId)	)
		{
			throw new ServiceException(ErrorMessageCode.PROVIDED_QUESTIONS_NOT_ASSOCIATED_WITH_PROVIDED_SURVEY);
		}
	}

}
