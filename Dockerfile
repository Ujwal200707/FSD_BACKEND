### Multi-stage Dockerfile for building and running the Spring Boot backend
### Stage 1 - build with Maven
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copy POM first to leverage layer caching for dependencies
COPY pom.xml ./
# If your repo includes the maven wrapper and .mvn, copy them as well
COPY mvnw ./
COPY .mvn .mvn

# Copy source and package
COPY src ./src

RUN mvn -DskipTests package -DskipUTs -DskipITs

### Stage 2 - runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
