# teste-oss-java

----

## Local environment

run `docker-compose up` to start database and migration.

## Metadata 
- [IRC-MTDT](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1331003434/Metadata+Spec)

This boilerplate is compliance with IRC-MTDT, IRC-LOG, IRC-DTB, IRC-WATCH, IRC-FAIL, FT-FTOGGLE and IRC-BAS but in case of changes reviews the standards and always keep the `spec.contents[*].certifications` updated.

if any Standards is not applicable, define as NOT_APPLICABLE in `spec.contents[*].certifications`.

## Monitoring 
- [IRC-WATCH](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1975550546/RFC+-+Monitoring)

"You Build it, you Run it"

You MUST create a documentation page for [Production Management](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1226212204/Documenta+o+de+Gest+o+de+Produ+o+de+Servi+o.) and add this documentation in metadata certifications.

You MUST follow [monitoring standards for new projects](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1105723464/Padr+o+de+Monitora+o#Novos-Projetos) to configure monitoring and alerts for your use case.

## Logs 
- [IRC-LOG](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/101449784/iFood+Logging+Standards)

Review your logs according [standard log levels](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/101449784/iFood+Logging+Standards#LoggingGuide-LogLevels).

You MUST NOT log sensitive information in ANY circumstances, for more information see: [Sensitive Information](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/101449784/iFood+Logging+Standards#Sensitive-information).

## Resilience
- [IRC-Fail](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1070301386/Resilience)

As suggested on IRC, review all timeouts configurations and circuit breakers configurations (`application.yaml` -> `resilience4j.circuitbreaker`).

Define your fallback strategy, for this example the `ifood.boilerplate.fallback.FallbackPublisher` will be called in failure case.

## Database
- [iFood PostgreSQL Access Standards](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1102381109/iFood+PostgreSQL+Access+Standards)

Read [IRC-DTB](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1102381109/iFood+PostgreSQL+Access+Standards) and check `application.yaml`, `application-dev.yaml` and `application-prod.yaml` configurations to your service use case.

Request on `#database-architecture` channel for new database to your service and change `jdbc.url` properties in `application-dev.yaml`, `application-prod.yaml` files.

Review `jdbc.username` property with is a correct database user and `jdbc.password` is the same name of Vault key.

## Feature flags
- [RFC - Feature Toggles](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1455849687/RFC+-+Feature+Toggles)
- [IRC-FTOGGLE | iFood Feature Toggles](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/2402190209/IRC-FTOGGLE+iFood+Feature+Toggles)
- [Feature Flags: Walkthrough for Applications](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1801684812/Feature+Flags+Walkthrough+for+Applications)

by default, the feature-flags sdk use a configuration file in `/toggles/feature-toggles.properties`.

All flags inserted in the FeatureFlag enum must be registered in Tompero. For canary deployment, all flags must be registered for `serviceName-canary`.

## Business audit

- [RFC - Business Audit](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1710260522/RFC+-+Business+Audit)
- [IRC-BAS | Business Audit Standards](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/1985840021/IRC-BAS+%7C+Business+Audit+Standards)

By default, a business audit is enabled and it depends on the local Kafka (`localhost:9092`), you can disable in `business.audit.disable:true` property (`application.yaml`).

For `development` and `production` environments, request Kafka topic credentials for this project by following: [Kafka Credentials for Business Audit](https://rwondemand.atlassian.net/wiki/spaces/EN/pages/2602893348/Credentials+for+Business+Audit+Kafka+Topic)