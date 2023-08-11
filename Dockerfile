FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp

RUN apk add --update curl && \
    rm -rf /var/cache/apk/*

RUN addgroup -S app && adduser -S app -G app
USER app

COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
