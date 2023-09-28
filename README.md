# DropwizardWebService

How to start the DropwizardWebService application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/bham1-backend-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`


How to run checkstyle for linting
---
1. Run 'mvn checkstyle:check'
1. It will also be run if you try to push your changes.

ENV Variables
---
1. Create the ENV variables below for the LoginIntegrationTest build configuration.
TARGET_DOMAIN
TEST_EMAIL
TEST_PWD
