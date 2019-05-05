package com.survey.microservice.surveyeeservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.survey.microservice.SurveyeeServiceApplication;
import com.survey.microservice.base.exception.ServiceException;
import com.survey.microservice.base.model.ErrorMessageCode;
import com.survey.microservice.surveyeeservice.constant.ServiceConstant;
import com.survey.microservice.surveyeeservice.dao.JpaSurveyeeVersionQuestionChoicesAnswersCountDao;
import com.survey.microservice.surveyeeservice.entity.SurveyeeVersionQuestionChoicesAnswersCountEntity;
import com.survey.microservice.surveyeeservice.facade.SurveyeeServiceFacade;
import com.survey.microservice.surveyeeservice.facade.SurveyeeServiceFacadeBean;
import com.survey.microservice.surveyeeservice.model.QuestionAnswer;
import com.survey.microservice.surveyeeservice.model.RespondSurveyModel;
import com.survey.microservice.surveyeeservice.model.SurveyMetaData;
import com.survey.microservice.surveyeeservice.model.SurveyQuestionChoiceMetaData;
import com.survey.microservice.surveyeeservice.model.SurveyQuestionMetaData;
import com.survey.microservice.surveyeeservice.service.api.SurveyeeService;
import com.survey.microservice.surveyeeservice.service.api.ValidationService;
import com.survey.microservice.surveyeeservice.service.impl.SurveyeeServiceBean;
import com.survey.microservice.base.matcher.SurviceExceptionCustomMatcher;

import lombok.extern.java.Log;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SurveyeeServiceApplication.class)
public class SurveyeeServiceApplicationTests extends SurveyeeServiceApplicationTestDataFactory {

 
 
	@MockBean
	JpaSurveyeeVersionQuestionChoicesAnswersCountDao jpaSurveyeeVersionQuestionChoicesAnswersCountDao;

	@MockBean
	HashOperations hashOperations;

	@Autowired
	private SurveyeeService surveyeeService;

	@Autowired
	@InjectMocks
	private SurveyeeServiceFacadeBean surveyeeServiceFacade;
	/**/
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	public SurveyeeServiceApplicationTests() throws ServiceException {
		resetIds();
	}

	@Before
	public void createSurveyModel() throws ServiceException {
		resetIds();
		when(hashOperations.get(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(modelSurveyMetaDataValue());
	}

	// @Test
	public void contextLoads() {
		log.info("context loaded successfully");
	}

//	@Test()
	public void test_answer_question_not_in_survey_meta_data_exception() throws ServiceException {

		expectedEx.expect(ServiceException.class);
		expectedEx.expect(new SurviceExceptionCustomMatcher(
				ErrorMessageCode.PROVIDED_QUESTIONS_NOT_ASSOCIATED_WITH_PROVIDED_SURVEY));
		RespondSurveyModel respondSurveyModel = new RespondSurveyModel();
		respondSurveyModel.setQuestionAnswers(answerAllQuestionAndWithOneQuestionDoesntBelongToTheSurvey());

		surveyeeServiceFacade.respondSurvey(1l, 1l, respondSurveyModel);
	}

	// @Test()
	public void answer_only_one_question_out_of_two() throws ServiceException {
		expectedEx.expect(ServiceException.class);
		expectedEx
				.expect(new SurviceExceptionCustomMatcher(ErrorMessageCode.NOT_ALL_SURVEY_QUESTIONS_HAS_BEEN_ANSWERED));
		RespondSurveyModel respondSurveyModel = new RespondSurveyModel();
		respondSurveyModel.setQuestionAnswers(answerOnlyOneQuestionOutOfTwo());
		surveyeeServiceFacade.respondSurvey(1l, 1l, respondSurveyModel);
	}

	// @Test()
	public void answer_all_questions_but_one_qustion_answer_is_not_associated_with_right_question()
			throws ServiceException {
		expectedEx.expect(ServiceException.class);
		expectedEx.expect(new SurviceExceptionCustomMatcher(
				ErrorMessageCode.PROVIDED_ANSWERS_ARE_NOT_ASSOCIATED_WITH_PROVIDED_QUESTION));
		RespondSurveyModel respondSurveyModel = new RespondSurveyModel();
		respondSurveyModel.setQuestionAnswers(answerAllQuestionsButOneChoiceIsNotAssociatedWithRightQuestion());
		surveyeeServiceFacade.respondSurvey(1l, 1l, respondSurveyModel);
	}

	@Test
	public void test_respond_survey_statistics() throws ServiceException {

		when(jpaSurveyeeVersionQuestionChoicesAnswersCountDao.getSurveyQuestionRecords(anyLong()))
				.thenReturn(prepareRecordsResultingOneToNineRatio());

		// question with id 1
		SurveyQuestionMetaData surveyQuestionMetaData = surveyeeService.loadSurveyMetaData(1l, 1l)
				.getSurveyQuestionMetaData(1l);

		surveyQuestionMetaData = surveyeeServiceFacade.getQuestionMetaDataWithStatistics(1l, 1l, 1l);

		assertEquals(new Long(10), surveyQuestionMetaData.getTotalNumberOfAllAnswers());
		assertEquals(new BigDecimal(10.00).setScale(2),
				surveyQuestionMetaData.getSurveyQuestionChoiceMetaData(1l).getPersentage());
		assertEquals(new BigDecimal(90.00).setScale(2),
				surveyQuestionMetaData.getSurveyQuestionChoiceMetaData(2l).getPersentage());

	}

	private List<SurveyeeVersionQuestionChoicesAnswersCountEntity> prepareRecordsResultingOneToNineRatio() {
		List<SurveyeeVersionQuestionChoicesAnswersCountEntity> surveyeeVersionQuestionChoicesAnswersCountEntityList = new ArrayList<>();
		surveyeeVersionQuestionChoicesAnswersCountEntityList.add(prepareRecord(1l, 1l, 1l));
		surveyeeVersionQuestionChoicesAnswersCountEntityList.add(prepareRecord(1l, 2l, 9l));

		return surveyeeVersionQuestionChoicesAnswersCountEntityList;

	}

	private SurveyeeVersionQuestionChoicesAnswersCountEntity prepareRecord(long questionId, long choiceId,
			long numberOfChoices) {
		SurveyeeVersionQuestionChoicesAnswersCountEntity surveyeeVersionQuestionChoicesAnswersCountEntity = new SurveyeeVersionQuestionChoicesAnswersCountEntity();
		surveyeeVersionQuestionChoicesAnswersCountEntity.setId(1l);
		surveyeeVersionQuestionChoicesAnswersCountEntity.setSurveyId(1l);
		surveyeeVersionQuestionChoicesAnswersCountEntity.setSurveyQuestionId(questionId);
		surveyeeVersionQuestionChoicesAnswersCountEntity.setSurveyQuestionChoiceId(choiceId);
		surveyeeVersionQuestionChoicesAnswersCountEntity.setSurveyQuestionChoiceCount(numberOfChoices);
		return surveyeeVersionQuestionChoicesAnswersCountEntity;
	}

	private Set<QuestionAnswer> answerAllQuestionsButOneChoiceIsNotAssociatedWithRightQuestion() {

		Set<QuestionAnswer> questionsAnswerSet = new HashSet<>();
		questionsAnswerSet.add(createQuestionAnswer(1l, 11l));
		questionsAnswerSet.add(createQuestionAnswer(2l, 3l));

		return questionsAnswerSet;
	}

	private Set<QuestionAnswer> answerOnlyOneQuestionOutOfTwo() {
		Set<QuestionAnswer> questionsAnswerSet = new HashSet<>();
		questionsAnswerSet.add(createQuestionAnswer(1l, 1l));
		return questionsAnswerSet;
	}

	private Set<QuestionAnswer> answerAllQuestionAndWithOneQuestionDoesntBelongToTheSurvey() {

		Set<QuestionAnswer> questionsAnswerSet = new HashSet<>();
		questionsAnswerSet.add(createQuestionAnswer(3l, 1l));
		questionsAnswerSet.add(createQuestionAnswer(23l, 1l));
		return questionsAnswerSet;
	}

	private QuestionAnswer createQuestionAnswer(long questionId, long answerId) {
		QuestionAnswer questionAnswer = new QuestionAnswer();
		questionAnswer.setQuestionId(questionId);
		questionAnswer.setChoiceId(answerId);
		return questionAnswer;
	}

	private SurveyMetaData modelSurveyMetaDataValue() {
		SurveyMetaData surveyMetaData = new SurveyMetaData();
		surveyMetaData.setSurveyCode(ServiceConstant.SURVEY_METADATA_CACHE_NAME);
		surveyMetaData.setSurveyId(1l);
		surveyMetaData.setSurveyVersion(1l);
		surveyMetaData.setTitle("Dummy Survey title");
		List<SurveyQuestionMetaData> surveyQuestionMetaDataList = new ArrayList<>();
		surveyMetaData.setSurveyQuestionMetaDataList(surveyQuestionMetaDataList);

		surveyQuestionMetaDataList.add(createFirstQuestionMetaData());
		surveyQuestionMetaDataList.add(createSecondQuestionMetaData());
		surveyMetaData.populateCachingMap();
		return surveyMetaData;
	}

	private SurveyQuestionMetaData createSecondQuestionMetaData() {
		SurveyQuestionMetaData surveyQuestionMetaData = new SurveyQuestionMetaData();
		surveyQuestionMetaData.setCode("QUS10000001");
		surveyQuestionMetaData.setId(++QUESTION_ID);
		surveyQuestionMetaData.setTitle("Do you like mango?");
		List<SurveyQuestionChoiceMetaData> surveyQuestionChoiceMetaDataList = new ArrayList<>();
		surveyQuestionMetaData.setSurveyQuestionChoiceMetaDataList(surveyQuestionChoiceMetaDataList);
		surveyQuestionChoiceMetaDataList.add(createYesAnswer());
		surveyQuestionChoiceMetaDataList.add(createYesAnswer());
		surveyQuestionChoiceMetaDataList.add(createNoAnswer());
		surveyQuestionChoiceMetaDataList.add(createNoAnswer());
		return surveyQuestionMetaData;
	}

	private SurveyQuestionMetaData createFirstQuestionMetaData() {
		SurveyQuestionMetaData surveyQuestionMetaData = new SurveyQuestionMetaData();
		surveyQuestionMetaData.setCode("QUS10000001");
		surveyQuestionMetaData.setId(++QUESTION_ID);
		surveyQuestionMetaData.setTitle("Do you like apples?");
		List<SurveyQuestionChoiceMetaData> surveyQuestionChoiceMetaDataList = new ArrayList<>();
		surveyQuestionMetaData.setSurveyQuestionChoiceMetaDataList(surveyQuestionChoiceMetaDataList);
		surveyQuestionChoiceMetaDataList.add(createYesAnswer());
		surveyQuestionChoiceMetaDataList.add(createYesAnswer());
		surveyQuestionChoiceMetaDataList.add(createNoAnswer());
		return surveyQuestionMetaData;
	}
	
	 @Bean
	    @Primary
	    @Profile("test")
	    public DataSource dataSource() {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName("org.h2.Driver");
	        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
	        dataSource.setUsername("sa");
	        dataSource.setPassword("sa");
	 
	        return dataSource;
	    }

}
