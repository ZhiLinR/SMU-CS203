# Use a base image that includes Java
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file generated by Maven or Gradle
COPY target/user-msvc-1.0.0.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
