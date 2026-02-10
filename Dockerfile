FROM openjdk:23
COPY ./target/music_albums_store-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]