FROM amd64/amazoncorretto:17-alpine

COPY ./build/libs/server-0.0.1-SNAPSHOT.jar /asap-server.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=local", "asap-server.jar"]