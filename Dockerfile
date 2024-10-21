#======================== build container ========================
FROM docker.io/library/maven:3.8.5-openjdk-17 AS builder

#ARG uid=0
#ARG gid=0
USER 0

#======================== add configs and jar ========================
RUN mkdir /workdir
RUN mkdir /workdir/downloads
WORKDIR /workdir

COPY ./src /workdir/src
COPY ./downloads/*.jar /workdir/downloads/
COPY ./pom.xml /workdir/

RUN mvn clean package -DskipTests

#======================== runtime container ========================
FROM registry.access.redhat.com/ubi9/openjdk-17-runtime

ENV WORK_PATH=/home/default

#======================== configure environment ========================
RUN mkdir -p /home/default
RUN mkdir -p /home/default/configs
RUN mkdir -p $WORK_PATH/downloads

COPY --from=builder /workdir/target/*.jar $WORK_PATH/
COPY --from=builder /workdir/downloads/opentelemetry-javaagent.jar $WORK_PATH/
COPY --from=builder /workdir/src/main/resources/* $WORK_PATH/configs/

#======================== run ========================

ENTRYPOINT ["java", "-javaagent:$WORK_PATH/opentelemetry-javaagent.jar", "-Dotel.service.name=dgt-cloud-demo", "-Dotel.resource.attributes=service.name=sample-code-serviceOwO", "-Dotel.exporter.otlp.protocol=grpc", "-Dotel.traces.exporter=otlp", "-Dotel.metrics.exporter=none", "-Dotel.logs.exporter=otlp", "-Dspring.config.location=$WORK_PATH/configs/application.yml","-jar", "$WORK_PATH/dgt-cloud-demo.jar"]