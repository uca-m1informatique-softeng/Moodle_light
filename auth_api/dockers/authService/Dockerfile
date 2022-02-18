FROM maven:3.8.3-openjdk-16
RUN groupadd --system springcred && useradd --system springcred -g springcred
USER springcred:springcred
ARG JAR_FILE=target/*.jar
WORKDIR /spring
COPY target/spring-boot-security-postgresql-0.0.1-SNAPSHOT.jar /spring/app.jar
COPY dockers/authService/settings.xml /usr/share/maven/conf/
COPY pom.xml /spring/pom.xml
ENTRYPOINT java -jar /spring/app.jar
 #mvn clean test;
#EXPOSE 8080