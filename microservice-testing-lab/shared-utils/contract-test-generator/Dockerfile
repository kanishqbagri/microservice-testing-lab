FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn ./.mvn

# Copy source code
COPY src ./src

# Make mvnw executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Create a non-root user
RUN addgroup --system app && adduser --system --ingroup app app

# Change ownership of the app directory
RUN chown -R app:app /app

# Switch to non-root user
USER app

# Expose port
EXPOSE 8085

# Run the application
CMD ["java", "-jar", "target/contract-test-generator-1.0.0.jar"] 