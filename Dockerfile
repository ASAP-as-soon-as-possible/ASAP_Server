FROM amd64/amazoncorretto:17-alpine

COPY ./build/libs/*.jar /asap-server.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "asap-server.jar"]