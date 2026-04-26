# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy shared module
COPY jobportal-common ./jobportal-common

# Copy employer service
COPY employer-service ./employer-service

# Build common first
RUN cd jobportal-common && mvn clean install -DskipTests

# Build employer-service
RUN cd employer-service && mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/employer-service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]