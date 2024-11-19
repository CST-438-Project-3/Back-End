# Use an official JDK runtime as a parent image
FROM eclipse-temurin:23-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy only the necessary files for Gradle build first (for caching)
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

# Grant execute permission to the Gradle wrapper
RUN chmod +x gradlew

# Download dependencies to leverage caching
RUN ./gradlew dependencies

# Copy the source code into the container
COPY src /app/src

# Run the Gradle build inside the container
RUN ./gradlew clean build

# Copy the generated JAR file to the working directory
RUN mv /app/build/libs/pantrypal-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
