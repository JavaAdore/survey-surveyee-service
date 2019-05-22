package com.survey.microservice.surveyeeservice.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.survey.microservice.base.exception.ServiceException;
import com.survey.microservice.base.feignclient.SurveyDefinitionFeignClient;
import com.survey.microservice.base.model.ErrorMessageCode;
import com.survey.microservice.surveyeeservice.constant.ServiceConstant;
import com.survey.microservice.surveyeeservice.dao.JpaSurveyeeVersionQuestionChoicesAnswersCountDao;
import com.survey.microservice.surveyeeservice.entity.SurveyeeVersionQuestionChoicesAnswersCountEntity;
import com.survey.microservice.surveyeeservice.model.RespondSurveyModel;
import com.survey.microservice.surveyeeservice.model.SurveyMetaData;
import com.survey.microservice.surveyeeservice.model.SurveyQuestionChoiceMetaData;
import com.survey.microservice.surveyeeservice.model.SurveyQuestionMetaData;
import com.survey.microservice.surveyeeservice.service.api.AsyncServiceManager;
import com.survey.microservice.surveyeeservice.service.api.SurveyeeService;

import lombok.extern.java.Log;

@Log
@Service
public class SurveyeeServiceBean  implements SurveyeeService    {

	private final HashOperations  hashOperations;
	private final AsyncServiceManager asyncServiceManager;
	private final SurveyDefinitionFeignClient surveyDefinitionFeignClient;
	private final JpaSurveyeeVersionQuestionChoicesAnswersCountDao jpaSurveyeeVersionQuestionChoicesAnswersCountDao;

	public SurveyeeServiceBean(HashOperations  hashOperations, AsyncServiceManager asyncServiceManager,
			SurveyDefinitionFeignClient surveyDefinitionFeignClient,
			JpaSurveyeeVersionQuestionChoicesAnswersCountDao jpaSurveyeeVersionQuestionChoicesAnswersCountDao) {
		super();
		this.hashOperations = hashOperations;
		this.asyncServiceManager = asyncServiceManager;
		this.jpaSurveyeeVersionQuestionChoicesAnswersCountDao=jpaSurveyeeVersionQuestionChoicesAnswersCountDao;
		this.surveyDefinitionFeignClient = surveyDefinitionFeignClient;
	}

	@Override
	
	public SurveyMetaData loadSurveyMetaData(Long surveyId, Long surveyVersion) throws ServiceException {   
		SurveyMetaData surveyMetaData = loadFromCache(surveyId, surveyVersion);
		if (null == surveyMetaData) {
			surveyMetaData =  getSurveyMetaData(surveyId, surveyVersion);
			if(null == surveyMetaData)
			{
				throw new ServiceException(ErrorMessageCode.UNABLE_TO_LOAD_SURVEY_META_DATA);
			}
			surveyMetaData.populateCachingMap();
			asyncServiceManager.putToCache(ServiceConstant.SURVEY_METADATA_CACHE_NAME,
					generateSurveyVersionCache(surveyId, surveyVersion), surveyMetaData);
		}

		return surveyMetaData;
	}
	
	private SurveyMetaData getSurveyMetaData(Long surveyId, Long surveyVersion) throws ServiceException {
		// when failure surveyDefinitionFeignClient will be called
 		return 	surveyDefinitionFeignClient.getSurveyMetaData(surveyId, surveyVersion);
	}

	private SurveyMetaData loadFromCache(Long surveyId, Long surveyVersion) {
		SurveyMetaData surveyMetaData = null;
		try {
			surveyMetaData = (SurveyMetaData) hashOperations.get(ServiceConstant.SURVEY_METADATA_CACHE_NAME,
					generateSurveyVersionCache(surveyId, surveyVersion));
		} catch (Exception ex) {
			
			log.warning("Unable to fetch SurveyMetaData from redis .. .. ");
			// just ignore
			// or alert some one
			
		}
		return surveyMetaData;
	}

	private String generateSurveyVersionCache(Long surveyId, Long surveyVersion) {
		return surveyId + "-" + surveyVersion;
	}

	@Transactional
	@Override
	public void respondSurvey(Long surveyId, Long surveyVersion, RespondSurveyModel respondSurveyModel) {
		// no problem in parallelism, I iterate of Set 
		respondSurveyModel.getQuestionAnswers().parallelStream().forEach(qa->{
			
			 if(!isQuestionChoiceAnsweredBefore(qa.getChoiceId()))
			 {
				 registeQuestionAnswer(surveyId,surveyVersion,qa.getQuestionId(),qa.getChoiceId());
				 
			 }else
			 {
				 increaseChoiceCount(qa.getChoiceId());
			 }
			
		});
		
		
	}

	private void increaseChoiceCount(Long choiceId) {
		// I assume more many instances will be created of this survice,
		// I will delegate the headache of update count synchronization to be in database level
		jpaSurveyeeVersionQuestionChoicesAnswersCountDao.increaseChoiceCount( choiceId);
	}

	private void registeQuestionAnswer(Long surveyId, Long surveyVersion, Long questionId, Long choiceId) {
	
		SurveyeeVersionQuestionChoicesAnswersCountEntity surveyeeVersionQuestionChoicesAnswersCountEntity = new SurveyeeVersionQuestionChoicesAnswersCountEntity();		
		surveyeeVersionQuestionChoicesAnswersCountEntity.setSurveyId(surveyId);
		surveyeeVersionQuestionChoicesAnswersCountEntity.setSurveyVersion(surveyVersion);
		surveyeeVersionQuestionChoicesAnswersCountEntity.setSurveyQuestionId(questionId);
		surveyeeVersionQuestionChoicesAnswersCountEntity.setSurveyQuestionChoiceId(choiceId);
		surveyeeVersionQuestionChoicesAnswersCountEntity.setSurveyQuestionChoiceCount(1L);
		jpaSurveyeeVersionQuestionChoicesAnswersCountDao.save(surveyeeVersionQuestionChoicesAnswersCountEntity);
	}

	private boolean isQuestionChoiceAnsweredBefore(Long choiceId) {
		return jpaSurveyeeVersionQuestionChoicesAnswersCountDao.findByChoiceId(choiceId).isPresent();
	}

	@Override
	public void populateSurveyResult(SurveyMetaData surveyMetaData) {
		List<SurveyeeVersionQuestionChoicesAnswersCountEntity> records=	jpaSurveyeeVersionQuestionChoicesAnswersCountDao.getSurveyRecords(surveyMetaData.getSurveyId(),surveyMetaData.getSurveyVersion());	
		if(!records.isEmpty())
		{
			Long tempQuestionId= -1l;
			for(int i=0;i<records.size() ; i++)
			{
				SurveyeeVersionQuestionChoicesAnswersCountEntity surveyeeVersionQuestionChoicesAnswersCountEntity = records.get(i);
				Long questionId   = surveyeeVersionQuestionChoicesAnswersCountEntity.getSurveyQuestionId();
				Long choiceId     = surveyeeVersionQuestionChoicesAnswersCountEntity.getSurveyQuestionChoiceId();
				Long choiceCount  = surveyeeVersionQuestionChoicesAnswersCountEntity.getSurveyQuestionChoiceCount();
				
				if(tempQuestionId!=-1 && !tempQuestionId.equals(questionId))
				{
					surveyMetaData.assignQuestionChoicesStatistics(tempQuestionId);
				}
				
				surveyMetaData.assignQuestionChoiceCount(questionId,choiceId,choiceCount);
				
				tempQuestionId = questionId;
			}
			// calculate statistics for last question 
			surveyMetaData.assignQuestionChoicesStatistics(tempQuestionId);

		}
	}

	@Override
	public SurveyQuestionMetaData populateSurveyQuestionMetaDataStatistics(SurveyQuestionMetaData surveyQuestionMetaData) {
		List<SurveyeeVersionQuestionChoicesAnswersCountEntity> records=	jpaSurveyeeVersionQuestionChoicesAnswersCountDao.getSurveyQuestionRecords(surveyQuestionMetaData.getId());	
		records.stream().forEach(r->{
			SurveyQuestionChoiceMetaData surveyQuestionChoiceMetaData = surveyQuestionMetaData.getSurveyQuestionChoiceMetaData(r.getSurveyQuestionChoiceId());
			surveyQuestionChoiceMetaData.setNumberOfAnswers(r.getSurveyQuestionChoiceCount());
		});
		surveyQuestionMetaData.assignQuestionChoicesStatistics();
		
		return surveyQuestionMetaData;
	}

	//TODO (remove) this method is for development only ,, will never published to production
	@Override
	public void deleteAllData() {
		jpaSurveyeeVersionQuestionChoicesAnswersCountDao.deleteAllInBatch();		
	}
 

	 

}
