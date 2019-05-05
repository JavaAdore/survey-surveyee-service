# survey-surveyee-service
service to responds surveys created by  <a href="https://github.com/JavaAdore/survey-definition-service">survey-definition-service</a> </br>
Mostly used by (Surveyors )

# prerequisites

survey eureka server should be up and run<br/>
<a href="https://github.com/JavaAdore/eureka-server">https://github.com/JavaAdore/survey-eureka-server</a> <br/>

survey-definition-service should be up and run<br/>
<a href="https://github.com/JavaAdore/survey-definition-service">https://github.com/JavaAdore/survey-definition-service</a> <br/>

redis server .. with no authentication

# Some parts this service code is copied from my open source service "user-service"
<a href="https://github.com/JavaAdore/user-service">https://github.com/JavaAdore/user-service</a>


zipkin server nice be up and run so you can track the request<br/>
<a href="https://github.com/JavaAdore/survey-zipkin-server">https://github.com/JavaAdore/survey-zipkin-server</a> <br/>



Postgres DB <br/>


environment variables should be added

# SURVEY_ZIPKIN_SERVER_IP = 127.0.0.1
127.0.0.1 the ip of machine where zipkin server runs
  

# SURVEY_POSTGRES_SERVER_IP    = 127.0.0.1
# SURVEY_POSTGRES_SERVER_PORT  = 5432
ensure postgres server runs under port 5432
# SURVEY_POSTGRES_DBNAME 	    = postgres     
 
  
# SURVEY_EUREKA_SERVER_IP      = 127.0.0.1
# SURVEY_EUREKA_SERVER_PORT    = 8761
# SURVEY_POSTGRES_USER         = postgres
# SURVEY_POSTGRES_PASS         = survey_db_password

# SURVEY_REDIS_SERVER_HOST      = 127.0.0.1
# SURVEY_REDIS_SERVER_PORT      = 6379
ensure redis works under port 6379


# survey-surveyee-service provides the following functionalities

SurveyMetaData loadSurveyMetaData(Long surveyId, Long surveyVersion);

void respondSurvey(Long surveyId, Long surveyVersion, RespondSurveyModel respondSurveyModel) throws ServiceException;

SurveyMetaData getSurveyMetaDataWithStatistics(Long surveyId, Long surveyVersion);

SurveyQuestionMetaData getQuestionMetaDataWithStatistics(Long surveyId, Long surveyVersion, Long questionId) throws ServiceException;

## General                                                                                                               
void deleteAllData();

                                                                                                                                                                                                                                  
# build
as root/Administration <br/>
build without dockerize : <br/>
mvn clean install <br/>
build and dockerize : <br/>
mvn clean install docker:removeImage docker:build


# run
java -jar target/survey-surveyee-service.jar
