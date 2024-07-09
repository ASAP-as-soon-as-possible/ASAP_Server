FROM amd64/amazoncorretto:17

WORKDIR /app

COPY ./build/libs/server-0.0.1-SNAPSHOT.jar /app/asap-server.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "asap-server.jar"]