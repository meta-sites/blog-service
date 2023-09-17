FROM openjdk:17-jdk-alpine

RUN apk update && apk add mysql-client
RUN mkdir /app
COPY api.jar /app
WORKDIR /app

ENTRYPOINT ["java","-jar","api.jar"]
