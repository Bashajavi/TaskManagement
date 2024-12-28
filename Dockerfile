#
# Build stage
#
FROM maven:3.9-amazoncorretto-17-al2023 AS build
WORKDIR /app

# Cache dependencies to speed up subsequent builds
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the application source code
COPY . .

# Build the application and skip tests for faster builds
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM amazoncorretto:17-alpine-jdk

# Set a non-root user for better security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Expose the default application port
EXPOSE 8080

# Set JVM options for better performance
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"

# Run the application with optimized JVM settings
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
