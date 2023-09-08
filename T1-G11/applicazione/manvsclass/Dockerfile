FROM openjdk:8-alpine as java8

# FROM openjdk:17-alpine
# COPY --from=java8 /usr/lib/jvm/java-1.8-openjdk /usr/lib/jvm/java-1.8-openjdk

RUN apk update && apk add bash
RUN apk add --no-cache openssl ncurses-libs libstdc++

COPY target/manvsclass-0.0.1-SNAPSHOT.jar /app/manvsclass.jar

WORKDIR /app

EXPOSE 8080
CMD ["java","-jar","manvsclass.jar"]