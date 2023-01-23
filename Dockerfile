FROM --platform=linux/amd64 gradle:7.6-jdk17-alpine as builder

WORKDIR /builder
COPY . .
# build jar
RUN gradle clean bootJar
# extract layers from built jar
RUN java -Djarmode=layertools -jar build/libs/epoc.jar extract --destination layers

FROM --platform=linux/amd64 azul/zulu-openjdk-alpine:17-jre

RUN adduser -u 1999 -D user

WORKDIR /app
# copy extracted layers from builder
COPY --from=builder /builder/layers/* ./

EXPOSE 8080

USER user

ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Xms128m", "-Xmx256m", "org.springframework.boot.loader.JarLauncher"]
