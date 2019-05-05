package com.survey.microservice.base.matcher;

 import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import com.survey.microservice.base.exception.ServiceException;
import com.survey.microservice.base.model.ErrorMessageCode;

public class SurviceExceptionCustomMatcher extends TypeSafeMatcher<ServiceException> {

 

    private ErrorMessageCode foundErrorCode;
    private final ErrorMessageCode expectedErrorCode;

    public SurviceExceptionCustomMatcher(ErrorMessageCode expectedErrorCode) {
        this.expectedErrorCode = expectedErrorCode;
    }

    @Override
    protected boolean matchesSafely(final ServiceException exception) {
        foundErrorCode = exception.getErrorMessageCode();
        return foundErrorCode  == expectedErrorCode;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(foundErrorCode)
                .appendText(" was not found instead of ")
                .appendValue(expectedErrorCode);
    }
}
