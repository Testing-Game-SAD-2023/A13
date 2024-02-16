FROM ubuntu:latest

COPY . /app

WORKDIR /app

RUN bash installazione.sh

RUN bash ./docker-entrypoint.sh

WORKDIR /app/Serv

EXPOSE 3080
ENTRYPOINT [ "node", "prova_esecuzione_parametri4.js" ] 