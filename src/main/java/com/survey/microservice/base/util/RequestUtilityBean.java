package com.survey.microservice.base.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class RequestUtilityBean {

	HttpServletRequest request;

	public RequestUtilityBean(HttpServletRequest request) {
		this.request = request;
	}
	
	public Locale getRequesterLocale() {
		if (null != request) {
			String localeAsString = request.getHeader("locale");
			if (null != localeAsString) {
				return new Locale(localeAsString);
			}
		}
		return Locale.ENGLISH;
	}

}
