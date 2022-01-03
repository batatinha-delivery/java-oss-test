FROM registry.infra.ifood-prod.com.br/ifood/docker-images/newrelic-java:latest as nrbuilder

FROM azul/zulu-openjdk-alpine:17

LABEL maintainer="IFood"
LABEL version="1.0"

EXPOSE 8080
EXPOSE 8081

RUN mkdir /newrelic
COPY --from=nrbuilder /nr/* /newrelic/

COPY target/*.jar teste-oss-java.jar
COPY entrypoint.sh ./entrypoint.sh
RUN chmod +x ./entrypoint.sh

ENTRYPOINT ["sh", "entrypoint.sh"]