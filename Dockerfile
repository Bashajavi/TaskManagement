#
# Build stage
#
FROM maven:3.9-amazoncorretto-17-al2023 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
# Copy the correct JAR file to the package stage and rename it to 'app.jar' for simplicity
COPY --from=build /target/TaskManager-3.3.5.jar /app.jar
# Expose the default port
EXPOSE 8080
# Use the correct file name in ENTRYPOINT
ENTRYPOINT ["java", "-jar", "/app.jar"]
