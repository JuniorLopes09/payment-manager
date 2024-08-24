FROM eclipse-temurin:17

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

WORKDIR /paymentmanager

ARG JAR_FILE=build/libs/*-SNAPSHOT.jar

COPY ${JAR_FILE} /paymentmanager/app.jar
# Expose the port your application will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
