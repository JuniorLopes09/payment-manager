FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN ./gradlew build

FROM openjdk:17-jdk-slim

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

EXPOSE 8080

COPY --from=build /build/libs/*.jar /paymentmanager/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
