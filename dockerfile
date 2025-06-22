# ── Build Stage ─────────────────────────────
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Maven wrapper and project files
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src ./src

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# ── Runtime Stage ───────────────────────────
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
