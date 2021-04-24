FROM java:8-jdk-alpine

EXPOSE 8080

ARG JAR_FILE

ADD target/${JAR_FILE} /waiter-service.jar

ENTRYPOINT ["java", "-jar", "/waiter-service.jar"]