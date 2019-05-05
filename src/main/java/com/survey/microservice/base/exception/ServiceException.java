package com.survey.microservice.base.exception;

import java.util.Arrays;

import com.survey.microservice.base.model.ErrorMessageCode;

import lombok.Getter;

@Getter
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	private ErrorMessageCode errorMessageCode;
	private Object[] params;
	private Object content;

	public ServiceException(ErrorMessageCode errorMessageCode) {
		this(errorMessageCode, null, null);
	}

	public ServiceException(ErrorMessageCode errorMessageCode, Object[] params) {
		this(errorMessageCode, null, params);
	}

	public ServiceException(ErrorMessageCode errorMessageCode, Object content, Object[] params) {
		this.errorMessageCode = errorMessageCode;
		this.content = content;
		this.params = params;

	}

	public String getCode() {
		if (null != errorMessageCode) {
			return errorMessageCode.getCode();
		}
		return null;
	}

	public String getMessageKey() {
		if (null != errorMessageCode) {
			return errorMessageCode.getMessageKey();
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((errorMessageCode == null) ? 0 : errorMessageCode.hashCode());
		result = prime * result + Arrays.deepHashCode(params);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceException other = (ServiceException) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (errorMessageCode != other.errorMessageCode)
			return false;
		if (!Arrays.deepEquals(params, other.params))
			return false;
		return true;
	}
}
