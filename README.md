# Tracking Media Collection

A simple java backend project for tracking servies (i.e. movies/series) using SpringBoot + Hibernate

## Features

## Things implemented/used :

- 3 Layer architecture (Controller <--> Service <--> Repository[DAO])
- REST Api's for CRUD Operations (Create, Read, Update, Delete)
- REST Api's for Pagination & Sorting
- Using DTO's (for data transfer)
- Validation Handling
- Exception Handling (for proper error message to user)
<!-- - JWT based authentication
- Role based authentication (diff access for admins & users)
- Api's for Login & Register -->

## Technologies/Tools used :

- Spring Boot Java Framework
- Maven Build Tool
- Visual Studio Code IDE
- Apache Tomcat (embedded web server in Spring)
- Spring Core
- Spring Data JPA (Hibernate)
- Mysql Workbench
- Postman Rest Client
<!-- - Swagger
- Spring Security (JWT) -->

## Screenshots

## VSCode

Extensions installed :

- Spring Boot Extension Pack
- Lombok Annotations Support for VS Code

Steps through command pallete (Ctrl+Shift+p) :

- Spring Initializr: Create a Maven Project
- Specific Spring Boot version - 3.0.4
- Specific project language - Java
- groupId - servie
- artifactId - track_servie
- Specific packaging type - Jar
- Specific Java version - 19
- Search for dependencies (reflects in pom.xml file):
  - spring-boot-starter-data-jpa
  - spring-boot-starter-thymeleaf
  - spring-boot-starter-web
  - spring-boot-devtools
  - mysql-connector-j
  - lombok
  - spring-boot-starter-validation
  - modelmapper
  <!-- - spring-boot-starter-security -->
  - spring-boot-starter-test (by default)
