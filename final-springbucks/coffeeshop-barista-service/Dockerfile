FROM java:8-jdk-alpine

EXPOSE 8080

ARG JAR_FILE

ADD target/${JAR_FILE} /barista-service.jar

ENTRYPOINT ["java", "-jar", "/barista-service.jar"]