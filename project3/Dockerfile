FROM openjdk:17-jdk

# Copy the built JAR file to the container
COPY target/project3.jar project3.jar

# Expose the port (for documentation purposes; not required for Cloud Run)
EXPOSE 8080

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "project3.jar"]
