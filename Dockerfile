FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir -p /var/lib/creator_server/resources
ENTRYPOINT ["java","-Dproject.resources=/var/lib/creator_server/resources","-jar","/app.jar"]