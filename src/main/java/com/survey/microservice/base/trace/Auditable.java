package com.survey.microservice.base.trace;

import com.survey.microservice.base.trace.entity.BaseAuditEntity;

public interface Auditable {

	
	BaseAuditEntity getAudit();
}
