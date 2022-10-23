FROM maven:3.8.6 AS builder

COPY ./ /
RUN mvn install

FROM openjdk:17-slim

RUN mkdir /opt/secure-api /etc/secure-api && \
    groupadd -g 1200 app && \
    adduser --system --shell /sbin/nologin --home /opt/secure-api --gid 1200 --uid 1200 app && \
    chown app:app /opt/secure-api /etc/secure-api

COPY --from=builder --chown=app:app  /target/api-security-test-0.1.jar /opt/secure-api/
COPY --chown=app:app /src/main/resources/*.properties /etc/secure-api

CMD java -jar /opt/secure-api/api-security-test-0.1.jar   --spring.config.location=/etc/secure-api/ --logging.config=/etc/secure-api/log4j2.properties -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager $JAVA_OPTS

EXPOSE 8080

