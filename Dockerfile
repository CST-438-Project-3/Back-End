# Use an official JDK runtime as a parent image
FROM eclipse-temurin:17-jdk
# Use Java 17 as it's widely supported and stable

# Set the working directory inside the container
WORKDIR /app

# Copy only Gradle wrapper and build files first for caching
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

# Run Gradle dependencies download step to leverage Docker layer caching
RUN ./gradlew dependencies || true

# Copy the source code
COPY src /app/src

# Build the application
RUN ./gradlew clean bootJar

# Copy the built JAR file to the app directory
RUN cp /app/build/libs/*.jar /app/app.jar

# Expose port 8080 for the application
EXPOSE 8080

# Run the application with the dynamic port (Heroku support)
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
