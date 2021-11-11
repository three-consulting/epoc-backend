FROM azul/zulu-openjdk-alpine:11-jre

RUN adduser -u 1999 -D user

COPY build/libs/epoc-0.0.1.jar /epoc.jar

EXPOSE 8080

USER user

ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Xms128m", "-Xmx128m", "-jar", "/epoc.jar"]
