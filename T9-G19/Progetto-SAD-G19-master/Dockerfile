FROM openjdk:8-alpine as java8

FROM openjdk:17-alpine
COPY --from=java8 /usr/lib/jvm/java-1.8-openjdk /usr/lib/jvm/java-1.8-openjdk

RUN apk update && apk add bash
RUN apk add --no-cache openssl ncurses-libs libstdc++


COPY . /app

WORKDIR /app

ENTRYPOINT [ "bash", "./docker-entrypoint.sh" ]
# ENTRYPOINT [ "/usr/lib/jvm/java-1.8-openjdk/bin/java", "-jar", "Task9-G19-0.0.1-SNAPSHOT.jar" ]
# ENTRYPOINT [ "bash", "robot.sh", "Lola", "5", "12312", "-1" ]