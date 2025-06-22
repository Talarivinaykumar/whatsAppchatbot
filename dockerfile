# ── Build Stage ─────────────────────────────
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy the Maven wrapper and project files
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# ── Runtime Stage ───────────────────────────
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
