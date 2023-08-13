# Stage 1: Build stage
FROM maven:3.8.3-openjdk-11-slim AS build
ARG ELASTICSEARCH_SERVICE
ARG MYSQL_SERVICE
ENV MYSQL_SERVICE=$MYSQL_SERVICE
ENV ELASTICSEARCH_SERVICE=$ELASTICSEARCH_SERVICE
COPY . /app
WORKDIR /app
RUN mvn clean package -Pprod

# Stage 2: Runtime stage
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/api.jar /app
ENTRYPOINT ["java", "-jar", "api.jar"]