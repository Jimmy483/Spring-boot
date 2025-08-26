FROM openjdk:18

WORKDIR /app

COPY target/learn-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 7071

CMD [ "java", "-jar", "app.jar"]
