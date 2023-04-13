# Medical-clinic

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Tools](#tools)
* [Setup](#setup)

## General info:
Application for management clinic. CRUD (Create, Read, Update, Delete) operations were used for the project using spring boot and an in-memory H2 database. 
Implemented methods allow additions, updates and resource search. In this program, the Spring Security security tool was additionally used,
in which roles were created to secure access to specific endpoints in the application. Swagger was used to better document and use REST API services.

## Technologies:
* Java
* Spring Framework (Spring Date, Spring Security)
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
