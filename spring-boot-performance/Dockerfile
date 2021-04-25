FROM java:8-jdk-alpine

EXPOSE 8080

ARG JAR_FILE

ADD target/${JAR_FILE} /performance-test.jar
ENV JVM_OPTS=""
ENTRYPOINT java ${JVM_OPTS}  -jar performance-test.jar
#ENTRYPOINT ["java", "-jar", "/performance-test.jar"]