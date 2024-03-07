FROM ubuntu:latest
# FROM openjdk:8-alpine as java8

# FROM openjdk:17-alpine
# COPY --from=java8 /usr/lib/jvm/java-1.8-openjdk /usr/lib/jvm/java-1.8-openjdk

RUN apt-get update && apt-get install -y openjdk-8-jdk bash
RUN apt-get install -y openssl libncurses5 libstdc++6

# RUN apk update && apk add bash
# RUN apk add --no-cache openssl ncurses-libs libstdc++

COPY target/manvsclass-0.0.1-SNAPSHOT.jar /app/manvsclass.jar
COPY installazione.sh /app
COPY evosuite-1.0.6.jar /app
COPY evosuite-standalone-runtime-1.0.6.jar /app

WORKDIR /app

RUN bash installazione.sh

#WORKDIR /app

EXPOSE 8080
CMD ["java","-jar","manvsclass.jar"]