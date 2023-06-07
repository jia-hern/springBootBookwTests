# Book library app

> Simple spring boot server

## Basic functionalities of this server:

```bash
Has apis for user, reader, book and record
Apis are protected with jwt authentication.
```

## Running the project locally:

```bash
1. Download and unzip project.<br>
   Open the project in a code editor e.g. Visual Studio Code.<br>
   Requires jdk 17 installed on your development setup.

2. In the root folder, contains postman_colleciton.json. import that into your local postman.
Contains the sample requests used to test the apis.

3. Using h2 as a volatile sql database for development.
For persistence storage, mysql/postgresql can be configured to be used instead.

4.
a) To run the server, type in terminal:
    mvn clean spring-boot:run

b) To access the h2 console:
http://localhost:8080/h2
Need to update the jdbc url to:
jdbc:h2:mem:book-library

c) Documentation of the endpoints can be found on:
http://localhost:8080/swagger-ui/index.html#
```

## Testing

```bash
Unit Tests of the service implementation can be found under:
src/test/java/com/myorg/booklibrary/serviceImpl

Integration Tests can be found under:
src/test/java/com/myorg/booklibrary/controller
```
