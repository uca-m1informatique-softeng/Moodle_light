FROM maven:3.8.3-openjdk-16
RUN groupadd --system spring && useradd --system spring -g spring
USER spring:spring
ARG JAR_FILE=target/*.jar
WORKDIR /spring
COPY target/spring-boot-security-postgresql-0.0.1-SNAPSHOT.jar /spring/app.jar
COPY pom.xml /spring/pom.xml
ENTRYPOINT java -jar /spring/app.jar; mvn clean test;
EXPOSE 8080