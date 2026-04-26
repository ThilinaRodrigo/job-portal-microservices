# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy shared module
COPY jobportal-common ./jobportal-common

# Copy job service
COPY job-service ./job-service

# Build common first
RUN cd jobportal-common && mvn clean install -DskipTests

# Build job-service
RUN cd job-service && mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/job-service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]