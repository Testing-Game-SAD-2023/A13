FROM golang:1.20.4-alpine as build

WORKDIR /app

COPY vendor .
COPY . .

RUN CGO_ENABLED=0 go build -ldflags "-s -w"  -o build/game-repository

FROM alpine 

WORKDIR /app

COPY --from=build /app/build/game-repository .

CMD ["/app/game-repository"]
