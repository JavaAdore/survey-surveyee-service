package com.survey.microservice.surveyeeservice.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
   
@Configuration
public class BeanConfig {

	
	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource
	      = new ReloadableResourceBundleMessageSource();
	    messageSource.setBasenames("classpath:messages");
	    // setting default encoding
	    messageSource.setDefaultEncoding("UTF-8");
	    return messageSource;
	}
	
 
	@Bean
    public JedisConnectionFactory jedisConnectionFactory() {

		RedisProperties properties = redisProperties();
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(properties.getHost());
		redisStandaloneConfiguration.setPort(properties.getPort());
		return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

	@Primary
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    @Primary
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }
    
    @Bean
    @Primary
    public HashOperations hashOperations()
    {
    	return redisTemplate().opsForHash();
    }
    
    
}
