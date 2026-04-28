### Multi-stage Dockerfile for building and running the Spring Boot backend
### Stage 1 - build with Maven
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copy POM and source (assumes no mvnw/.mvn in the repo)
COPY pom.xml ./
COPY src ./src

# Build the project inside the Maven image
RUN mvn -DskipTests package

### Stage 2 - runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
