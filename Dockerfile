# Use a lightweight Java runtime image suitable for running a Spring Boot application
FROM openjdk:17-jdk-slim

# Add metadata to the image
LABEL authors="User"

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
# Replace 'vessel-manager-01-0.0.1-SNAPSHOT.jar' with your JAR file name
COPY target/vessel-manager-01-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's default port
EXPOSE 8080

# Define the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
