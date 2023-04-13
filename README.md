# Medical-clinic

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Tools](#tools)
* [Setup](#setup)

## General info:
This application is used to manage the clinic. The implemented methods use CRUD operations (Create, Read, Update, Delete), which allow you to modify 
the clinic's resources. It was mainly based on the Spring Framework and the H2 database.  In addition, access to resources/endpoints was secured 
by granting appropriate permissions to users.

## Technologies:
* Java
* Spring Framework (Spring Boot, Spring Date, Spring Security)
* H2 Database

## Tools:
* Eclipse or IntelliJ IDEA (or any preferred IDE) with embedded Maven
* Maven
* Postman (or any RESTful API testing tool)
* Swagger can be launched in Browser: http://localhost:8080/other/swagger-ui.html

## Setup:
To run this project, install it locally using npm:

```
$ java -jar ~/path-medical-clinic/target/medical-clinic-1.0.0-SNAPSHOT
$ mvn spring-boot:run
$ mvn clean install
```
