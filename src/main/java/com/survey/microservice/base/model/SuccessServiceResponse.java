package com.survey.microservice.base.model;

public class SuccessServiceResponse<T> extends ServiceResponse<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 	public SuccessServiceResponse(T body) {
		super(ErrorMessageCode.OPERATION_DONE_SUCCESSFULLY.getCode(), ErrorMessageCode.OPERATION_DONE_SUCCESSFULLY.getMessageKey(), body);
	}
	
	 

}
