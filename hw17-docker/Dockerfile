FROM azul/zulu-openjdk:17.0.0-17.28.13-jre
COPY /target/hw17-docker-0.0.1-SNAPSHOT-deps.jar /hw17/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/hw17/app.jar"]