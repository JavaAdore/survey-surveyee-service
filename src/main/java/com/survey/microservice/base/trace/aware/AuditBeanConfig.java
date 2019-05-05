package com.survey.microservice.base.trace.aware;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

 
@Component
public class AuditBeanConfig implements AuditorAware<Long> {

	public Optional<Long> getCurrentAuditor() {
		// from security context holder return the id of the current logged user
		
	    return  Optional.ofNullable(null);
	  }
}
