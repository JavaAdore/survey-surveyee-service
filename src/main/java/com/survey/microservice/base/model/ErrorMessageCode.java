package com.survey.microservice.base.model;

public enum ErrorMessageCode {
	
	OPERATION_DONE_SUCCESSFULLY("00","OPERATION_DONE_SUCCESSFULLY"),
	GENERAL_BACKEND_ERROR("-1","GENERAL_BACKEND_ERROR"), 
	NOT_ALL_SURVEY_QUESTIONS_HAS_BEEN_ANSWERED("2002","NOT_ALL_SURVEY_QUESTIONS_HAS_BEEN_ANSWERED"), 
	PROVIDED_QUESTIONS_NOT_ASSOCIATED_WITH_PROVIDED_SURVEY("20003","PROVIDED_QUESTIONS_NOT_ASSOCIATED_WITH_PROVIDED_SURVEY"), 
	PROVIDED_ANSWERS_ARE_NOT_ASSOCIATED_WITH_PROVIDED_QUESTION("20004","PROVIDED_ANSWERS_ARE_NOT_ASSOCIATED_WITH_PROVIDED_QUESTION"), 
	UNABLE_TO_LOAD_SURVEY_META_DATA("20005","UNABLE_TO_LOAD_SURVEY_META_DATA"), 


	
	
	;
	
	private String code;
	private String messageKey;
	
	

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	ErrorMessageCode()
	{
		
	}
	
	ErrorMessageCode(String code)
	{
		this.code= code;
	}
	
	ErrorMessageCode(String code,String messageKey)
	{
		this.code= code;
		this.messageKey = messageKey;
	}
	
	public String getCode() {
		return code;
	}
	
}
