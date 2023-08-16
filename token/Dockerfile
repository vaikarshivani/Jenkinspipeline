# Use an official Maven image as the base image
FROM maven:3.8.4-openjdk-11 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the source code into the container
COPY . /app

# Build the Maven project
RUN mvn clean package

# Use an official OpenJDK image as the base image for running the Java application
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the previous stage into the container
COPY --from=build /app/target/*.jar /app/

# Run the Java application
CMD ["java", "-jar", "*.jar"]
