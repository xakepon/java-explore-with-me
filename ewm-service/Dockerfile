FROM amazoncorretto:21-alpine-jdk
VOLUME /tmp
COPY target/*.jar ewm-service.jar
ENTRYPOINT ["java","-jar","/ewm-service.jar"]
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
