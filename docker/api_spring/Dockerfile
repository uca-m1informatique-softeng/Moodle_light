FROM maven:3.8.3-openjdk-16
RUN groupadd --system spring && useradd --system spring -g spring
USER spring:spring
ARG JAR_FILE=target/*.jar
WORKDIR /spring
COPY target/app.jar /spring/app.jar
COPY pom.xml /spring/pom.xml
COPY docker/api_spring/settings.xml /usr/share/maven/conf/
ENTRYPOINT java -jar /spring/app.jar;
EXPOSE 8080