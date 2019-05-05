package com.survey.microservice.surveyeeservice.service.api;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Log
@Service
public class AsyncServiceManager {

	
	private final  RedisTemplate<String,Object> redisTemplate;

	public AsyncServiceManager(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@Async
	public void putToCache(String cacheName , String cacheKey,Object obj )
	{
		try
		{
			redisTemplate.opsForHash().put(cacheName, cacheKey, obj);
		}catch(Exception ex)
		{
			// connection error happend with redis
			log.warning("UNABLE TO CONNECT TO REDIS SERVICE TO CACHE OBJECT .. Probably Network issue or redis is not started or pointing to wrong host ");
		}
	}

}
