# Builder stage
FROM gradle:7.6.1-jdk17-alpine AS builder
WORKDIR /app

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG AWS_SCHEDULER_ROLE_ARN
ARG AWS_SCHEDULER_SQS_ARN

ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
ENV AWS_SCHEDULER_ROLE_ARN=${AWS_SCHEDULER_ROLE_ARN}
ENV AWS_SCHEDULER_SQS_ARN=${AWS_SCHEDULER_SQS_ARN}

COPY . .
RUN gradle clean build
RUN mv /app/build/libs/*.jar /app/build/libs/app.jar

# Run stage
FROM amazoncorretto:17-alpine3.17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]