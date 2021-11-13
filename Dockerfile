FROM azul/zulu-openjdk-alpine:17-jre

RUN adduser -u 1999 -D user

COPY build/libs/epoc.jar /epoc.jar

EXPOSE 8080

USER user

ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Xms128m", "-Xmx128m", "-jar", "/epoc.jar"]
