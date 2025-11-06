FROM amazoncorretto:25.0.1-alpine3.22

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 5001

ENTRYPOINT ["java", "-jar", "app.jar"]