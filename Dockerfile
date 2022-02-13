FROM openjdk:16
RUN groupadd --system spring && adduser --system spring -g spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY target/spring-boot-security-postgresql-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT java -jar /app.jar