package com.survey.microservice.base.config;

import java.util.Locale;

import org.springframework.stereotype.Service;

import lombok.Getter;

@Getter
@Service
public class BaseServiceConstant {

 
	
	private String DEFAULT_LOCALE=Locale.ENGLISH.getLanguage();
	
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	public static final String GLOBAL_SUCCESS_CODE = "00";
 
 
}
