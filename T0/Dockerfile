FROM eclipse-temurin:17-jdk-alpine

RUN apk add --no-cache bash maven

WORKDIR /generator
RUN mkdir -p /generator/EvosuiteGenerator /generator/RandoopGenerator

COPY EvosuiteGenerator /generator/EvosuiteGenerator
COPY RandoopGenerator /generator/RandoopGenerator

RUN cd /generator/RandoopGenerator && mvn dependency:resolve

RUN chmod +x /generator/EvosuiteGenerator/generate.sh \
    && chmod +x /generator/RandoopGenerator/generate.sh

CMD ["bash", "-c", "while true; do sleep 30; done"]

