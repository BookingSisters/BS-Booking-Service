# Builder stage
FROM gradle:7.6.1-jdk17-alpine AS builder
WORKDIR /app
COPY . .
RUN gradle clean build
RUN mv /app/build/libs/*.jar /app/build/libs/app.jar

# Run stage
FROM amazoncorretto:17-alpine3.17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]