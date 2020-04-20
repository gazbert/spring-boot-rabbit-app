# Spring Boot Rabbit App

[![Build Status](https://travis-ci.org/gazbert/spring-boot-rabbit-app.svg?branch=master)](https://travis-ci.org/gazbert/spring-boot-rabbit-app)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=gazbert_spring-boot-rabbit-app&metric=alert_status)](https://sonarcloud.io/dashboard?id=gazbert_spring-boot-rabbit-app)

## What is this?
A [Spring Boot](http://projects.spring.io/spring-boot/) sample app (and learning exercise!) 
that integrates [RabbitMQ](https://www.rabbitmq.com/) using [Spring AMQP](https://spring.io/projects/spring-amqp).

It could be used as boilerplate for developing microservices that produce and consume 
[AMQP](https://en.wikipedia.org/wiki/Advanced_Message_Queuing_Protocol) messages.

AMQP is an open standard wire specification for asynchronous message communication. AMQP is a
platform-neutral binary protocol standard - libraries can be written in different programming 
languages and run on different environments.

AMQP is made up of exchanges, queues, and bindings. Clients publish a message to an AMQP exchange
with a routing key. Queues are bound to an exchange using a routing key. The exchange then 
distributes the messages to the interested queues.

This sample app will demonstrate how to use and test 3 different exchange types:

* Direct Exchange – routes messages to a queue by matching a complete routing key.
* Fanout Exchange – routes messages to all the queues bound to it.
* Topic Exchange – routes messages to multiple queues by matching a routing key to a pattern.

## Install Rabbit
The fastest way to get up and running is to use the [Docker image](https://registry.hub.docker.com/_/rabbitmq/):

```bash
docker pull rabbitmq;
docker run -d -p 5672:5672 -p 15672:15672 --name my-rabbit rabbitmq:3-management
```

Once it's up and running, you can access the management UI using: 
[http://localhost:15672](http://localhost:15672)

The default login credentials are `guest/guest`

The alternative is to install it (and Erlang) natively as per the [official guide](https://www.rabbitmq.com/download.html).

## Build Guide
You'll need JDK 11 installed on your dev box.

You can use [Gradle](https://gradle.org/) or [Maven](https://maven.apache.org) to build the app.

Both [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and 
[Maven Wrapper](https://github.com/takari/maven-wrapper) are included in the project.

### Gradle
1. From the project root, run `./gradlew build`
1. To generate the Javadoc, run `./gradlew javadoc` and look in the `./build/docs/javadoc` folder.

### Maven
1. From the project root, run `./mvnw clean install`
1. Take a look at the Javadoc in the `./target/apidocs` folders after the build completes.

### Checkstyle
The app uses the [Google Style Guide](https://google.github.io/styleguide/javaguide.html)
which is enforced during both the Gradle and Maven build - see the [build.gradle](./build.gradle) 
and [pom.xml](./pom.xml) files respectively. The Checkstyle report locations are:

* Gradle - `./build/reports/checkstyle/main.html`
* Maven - `./target/checkstyle-result.xml`

### Code Coverage
Code coverage is provided by [JaCoCo](https://www.eclemma.org/jacoco/) and is enforced at build time.
It's currently set to 80% line coverage. See the build files. The coverage report locations are:

* Gradle - `./build/report/jacoco/test/html/index.html`
* Maven - `./target/jacoco-report/index.html`

### Code Quality
[SpotBugs](https://spotbugs.github.io/) is run at build time. Any bugs found will fail the build. 
The bug report locations are:

* Gradle - `./build/report/jacoco/test/html/index.html`
* Maven - `./target/spotbugsXml.xml`

### Tests
Unit tests are run as part of both Gradle and Maven builds.

[JUnit 4](https://junit.org/junit4/) and [Mockito](https://site.mockito.org/) is used to unit test
the code.

The unit test report locations are:
* Gradle - `build/reports/tests/test/index.html`
* Maven - `./target/surefire-reports`

The integration tests require a running instance of Rabbit -
[Testcontainers](https://www.testcontainers.org/) is used to achieve this. You'll need to stop any
other instance of Rabbit that you have running before running the tests, otherwise they will fail 
to due to port clashes.

The IT tests are located 
[here](./src/integration-test/java/com/gazbert/rabbitsample/it).

The Spring profile must be set to 'integration-test' in the 
[./config/application.properties](./config/application.properties) file:
```properties
spring.profiles.active=integration-test
```
 
To run the IT tests:
* Gradle - `./gradlew integrationTests`
* Maven  `./mvnw clean install -Pint`

The IT report locations are:
* Gradle - `./build/reports/tests/integrationTests/index.html`
* Maven - `./target/failsafe-reports`
 
## Configuration
The configuration is held in the [./config/application.properties](./config/application.properties) 
file.

```properties
# Connection Factory details.
amqp.connection.hostname=localhost
amqp.connection.port=5672
amqp.connection.username=guest
amqp.connection.password=guest
        
# Set to false to stop error messages going back onto head of queue and looping forever!
spring.rabbitmq.listener.simple.default-requeue-rejected=false

# Uncomment for the appropriate ErrorHandlingApp demo being run.
# The Rabbit Docker container will need to be restarted after each change to remove the queues.
amqp.configuration.current=simple-dlq
#amqp.configuration.current=custom-dlx
#amqp.configuration.current=parking-lot-dlx
```

If the `amqp.connection.*` properties are not set, Spring AMQP will use defaults: $hostname, 5672 and
guest/guest for credentials.

More details on the `amqp.configuration.*` properties can be found in the 
[_User Guide_](#running-the-error-handling-app) section below.

## User Guide

There are 5 demos:

1. A simple "Hello World!" app.
1. A publish messages example using Direct, Fanout, and Topic exchanges. 
1. Error Handling using a simple Dead Letter Queue
   ([DLQ](https://docs.spring.io/autorepo/docs/spring-cloud-stream-binder-rabbit-docs/1.1.1.RELEASE/reference/html/rabbit-dlq-processing.html)).
1. Error handling using a custom Dead Lead Exchange ([DLX](https://www.rabbitmq.com/dlx.html)).
1. Error handling using a 
   [Parking Lot Queue](https://docs.spring.io/autorepo/docs/spring-cloud-stream-binder-rabbit-docs/1.1.1.RELEASE/reference/html/rabbit-dlq-processing.html).

You'll need to restart the Rabbit Docker container after running each demo app in order to remove 
the queues.

```bash
docker container restart CONTAINER_ID
```

You can view the exchanges, queues, and messages in the Rabbit UI: 
[http://localhost:15672](http://localhost:15672)

### "Hello World!" app
The [Hello World!](./src/main/java/com/gazbert/rabbitsample/helloworld/Application.java) app is 
the most basic example of a Spring Boot app using Spring AMQP - a good place to start.

From the the app from the project root folder using:

* Gradle - `./gradlew bootRun`
* Maven - `./mvnw spring-boot:run`
   
### Publish Message app
The [Publish Message](./src/main/java/com/gazbert/rabbitsample/publish/PublishMessageApp.java) app
will publish a message to a Direct, Fanout, and Topic Exchange. Message consumers will read the 
message off the appropriate queue and log its contents.

From the project root folder using:

* Gradle - `./gradlew bootRun -PmainClass=com.gazbert.rabbitsample.publish.PublishMessageApp`
* Maven - `./mvnw spring-boot:run -Dstart-class=com.gazbert.rabbitsample.publish.PublishMessageApp`

### Error Handling app
The [Error Handling](./src/main/java/com/gazbert/rabbitsample/errorhandling/ErrorHandlingApp.java) 
app can be run using 3 different configurations:

1. Simple DLQ - a message is published to a Direct exchange. The Consumer reads the message off the
   queue and then throws a business exception. This exception is then routed to a DLQ where a 
   Consumer reads the failed message and logs it.
1. Custom DLX - a business exception is thrown as in the previous example and sent to a DLQ. This 
   time, the Consumer attempts to re-send the message 3 times before giving up and discarding it.
1. Parking Lot - as in the previous example, the failed message is re-sent 3 times, but instead of
   discarding it, the Consumer places the failed message on the Parking Lot Queue for manual
   investigation.

You'll need to update the [./config/application.properties](./config/application.properties) file
and set the `amqp.configuration.current` property to the example you want to run:

```properties
# Valid values are: simple-dlq, custom-dlx, parking-lot-dlx
amqp.configuration.current=simple-dlq
```

Run the app from the project root folder using:

* Gradle - `./gradlew bootRun -PmainClass=com.gazbert.rabbitsample.errorhandling.ErrorHandlingApp`
* Maven - `./mvnw spring-boot:run -Dstart-class=com.gazbert.rabbitsample.errorhandling.ErrorHandlingApp`
   
## Logging
Logging for the app is provided by [log4j](http://logging.apache.org/log4j). 
The log file is written to `logs/app.log` using a rolling policy. When a log file size reaches 
100 MB or a new day is started, it is archived and a new log file is created. 

The app will create up to 7 archives on the same day; these are stored in a directory based on the 
current year and month. Only the last 90 archives are kept. Each archive is compressed using gzip.

The logging level is set at `info`. You can change this default logging configuration in 
the [`config/log4j2.xml`](./config/log4j2.xml) file.

## Issue & Change Management
Issues and new features are managed using the project 
[Issue Tracker](https://github.com/gazbert/spring-boot-rabbit-app/issues) - submit bugs here.
 
You are welcome to take on new features or fix bugs! See [here](CONTRIBUTING.md) for how to get involved. 

## Credits
This sample app is based off the excellent [Spring AMQP tutorials](https://github.com/eugenp/tutorials/tree/master/spring-amqp)
by [Baeldung](https://www.baeldung.com/author/baeldung/).

## References
1. The [Spring AMQP](https://docs.spring.io/spring-amqp/reference/html/) documentation.
1. [Routing Topologies](https://spring.io/blog/2011/04/01/routing-topologies-for-performance-and-scalability-with-rabbitmq/) and best practice.
1. The official [Spring AMQP Sample](https://github.com/spring-projects/spring-amqp-samples) apps.

