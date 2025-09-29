FROM openjdk:17-jdk-slim

# Install Python for ML inference
RUN apt-get update && apt-get install -y python3 python3-pip && rm -rf /var/lib/apt/lists/*

# Install Python ML libraries
RUN pip3 install scikit-learn pandas numpy joblib

# Copy application
COPY target/synthetic-data-generator-1.0.0.jar app.jar
COPY trained-models/ /app/models/
COPY deployment/ /app/deployment/

# Set environment
ENV JAVA_OPTS="-Xmx2g -Xms1g"
ENV MODELS_DIR="/app/models"

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "/app.jar"]
