# Use an official OpenJDK runtime as a base image
FROM openjdk:11-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the target folder into the container
# Replace 'vote-cast-backend-0.0.1-SNAPSHOT.jar' with your actual JAR file name
COPY target/vote-cast-backend-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Define the command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
