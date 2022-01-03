#!/bin/sh

if [ -d /vault/secrets ]; then for i in $(ls -1 /vault/secrets); do source /vault/secrets/$i; done; fi

java ${JAVA_OPTS} \
    "-javaagent:/newrelic/newrelic.jar" \
    "-Dnewrelic.config.file=/newrelic/newrelic.yml" \
     -jar "teste-oss-java.jar"