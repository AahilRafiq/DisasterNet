FROM amazoncorretto:25.0.1-alpine3.22

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-cp", "app.jar", "org.nitk.NotifWorker"]