FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/contactapi-0.0.1-SNAPSHOT.jar /app/contact-list.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/contact-list.jar"]
