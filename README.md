# t-moviesflix-api

The API service is for those of you interested in using our movie, TV show or actor images and/or data in your application. 

## Technologies used

- Spring Boot 2(with Spring Data JPA, Spring Security, Spring Mail, Caching)
- MYSQL
- jjwt 0.9.1
- Other services: Docker, Paypal, Cloudinary

## API Documentation - OpenAPI 3 Specification:
- [Click here](http://tmoviesdev-env.eba-a32f99nt.ap-southeast-1.elasticbeanstalk.com/swagger-ui/index.html) to view all API endpoints

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `de.codecentric.springbootsample.Application` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```
