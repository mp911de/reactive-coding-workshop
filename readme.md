# Reactive Coding Workshop with Spring Boot

This repository contains code and [lab documentation](labdocs/labdocs.adoc) for a reactive coding workshop using Spring Boot.

This project is the skeleton (and solution) for the Reactive coding with Spring workshop.

The aim of the workshop is to discover Spring's Reactive features through a practical example.

The application exposes a HTTP API, based on Spring Boot and layered into Controllers and Services.

## Running the Code

This is a Maven project, developed and tested with IntelliJ IDEA.

The API is fully functional and can be played with (eg. with a REST client like POSTMAN) by running the Main class `org.dogepool.reactiveboot.ReactiveCodingWorkshopApplication`.

Controllers are unit tested (see `src/test/java`) and attendees are encouraged to run tests from within the IDE to check that their modifications at each step are correct. You shouldn't run all tests at once in your IDE as the embedded MongoDB process is retained between test runs and might cause issues. Run each test individually.
 
## License

This code is licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
