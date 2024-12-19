FROM openjdk:17-slim
WORKDIR /app
COPY target/contactapi-0.0.1-SNAPSHOT.jar /app/contact-list.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/contact-list.jar"]
