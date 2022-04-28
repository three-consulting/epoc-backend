FROM gradle:jdk17 as builder

WORKDIR /builder
COPY . .
# build jar
RUN gradle bootJar
# extract layers from built jar
RUN java -Djarmode=layertools -jar build/libs/epoc.jar extract --destination layers

FROM azul/zulu-openjdk-alpine:17-jre

RUN adduser -u 1999 -D user

WORKDIR /app
# copy extracted layers from builder
COPY --from=builder /builder/layers/dependencies ./
COPY --from=builder /builder/layers/spring-boot-loader ./
COPY --from=builder /builder/layers/snapshot-dependencies ./
COPY --from=builder /builder/layers/application ./

EXPOSE 8080

USER user

ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Xms128m", "-Xmx256m", "org.springframework.boot.loader.JarLauncher"]
