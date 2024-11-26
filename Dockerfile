# Stage 1: Build the application
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy necessary files for the build
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src

# Run the build process
RUN chmod +x gradlew && ./gradlew clean bootJar

# Stage 2: Create the final runtime image
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Use environment variable for server port (for Heroku support)
CMD ["java", "-jar", "app.jar", "--server.port=${PORT}"]
