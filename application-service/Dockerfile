# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy shared module
COPY jobportal-common ./jobportal-common

# Copy application service
COPY application-service ./application-service

# Build common first
RUN cd jobportal-common && mvn clean install -DskipTests

# Build application-service
RUN cd application-service && mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/application-service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]