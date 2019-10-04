# spring-boot-heroku-demo

[![Build Status](https://travis-ci.org/britter/spring-boot-heroku-demo.svg?branch=master)](https://travis-ci.org/britter/spring-boot-heroku-demo)
[![Coverage Status](https://coveralls.io/repos/britter/spring-boot-heroku-demo/badge.svg?branch=master&service=github)](https://coveralls.io/github/britter/spring-boot-heroku-demo?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

This is a small demo application for showing how to run a [Spring Boot](http://projects.spring.io/spring-boot/)
application on [Heroku](http://heroku.com). For more information have a look at the
[accompanying blog post](https://blog.codecentric.de/en/2015/10/deploying-spring-boot-applications-to-heroku).
You can test the application [here](http://spring-boot-heroku-demo.herokuapp.com). Note that it is only running on a
[free dyno](https://www.heroku.com/pricing), so it may take some time before it responds.

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

## Running the application

To run the application from your IDE, simply run the `com.github.britter.springbootherokudemo.Application` class as
a Java Application.
Alternatively the application can be started from the terminal using [gradle](https://gradle.org) with `./gradlew bootRun`.
After starting the application, point your browser to http://localhost:8080.

## Using a Postgres database for persistence

For running the application using a real [Postgres](http://www.postgresql.org/) database, the `postgres` application
profile defined by the `application-postgres.properties` file can be used. You can either change the file to point to
your pre existing Postgres instance or use a [Docker](http://docker.com) Postgres container.
To start Postgres in Docker just run `./gradlew dockerRun` and it will create a Postgres container for you.
After that you can start the application with `postgres` profile by running `./gradlew bootRun -Ppostgres`

## License

Code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt).
