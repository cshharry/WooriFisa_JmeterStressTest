# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file to the container
COPY build/libs/demo-0.0.1-SNAPSHOT.jar /app/myapp.jar

# Expose the application port (Spring Boot default is 8080)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]