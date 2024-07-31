# Project Title

Java Spring Boot Application for expense tracking


## Table of Contents
1. [Introduction](#introduction)
2. [Technologies](#technologies)
3. [Features](#features)
4. [Installation](#installation)
5. [Usage](#usage)
6. [API Documentation](#api-documentation)
7. [Testing](#testing)
8. [License](#license)


## Introduction
This project is a Java Spring Boot application designed for managing wallets, savings bills and expense tracking.
It implements JWT tokens for security, and includes features such as email verification for registration and password recovery,
and secure transactions between accounts. The application is RESTful and communicates using JSON


## Technologies
This project is built using the following technologies:

- **Spring Boot**: A framework that simplifies the development of Java applications.
    - `spring-boot-starter-data-jpa`
    - `spring-boot-starter-security`
    - `spring-boot-starter-validation`
    - `spring-boot-starter-mail`
    - `spring-boot-starter-web`
    - `spring-boot-starter-thymeleaf`
    - `spring-boot-starter-test`
- **JWT (JSON Web Tokens)**: Used for securing the application.
    - `jjwt-api`
    - `jjwt-impl`
    - `jjwt-jackson`
- **SpringDoc OpenAPI**: For API documentation.
    - `springdoc-openapi-starter-webmvc-ui`
- **Liquibase**: Database migration tool.
    - `liquibase-core`
- **PostgreSQL**: The database used for data storage.
    - `postgresql`
- **Testcontainers**: For integration testing with PostgreSQL.
    - `org.testcontainers.postgresql`
- **MapStruct**: For mapping between DTOs and entities.
    - `mapstruct`
    - `lombok-mapstruct-binding`
- **Lombok**: For reducing boilerplate code.
    - `lombok`
- **Thymeleaf**: For rendering HTML template in email letter.

## Features
- **Security**: JWT tokens (access & refresh) for authentication and authorization.
- **Email Notifications**: Email verification for registration and password recovery.
- **Wallet Management**: Create, delete and manage wallets and savings bills.
- **Transactions**: Transfer funds between wallets and savings accounts.
- **Operation Logging**: Record operations such as payments and deposits.
- **Operation Viewing**: Receive all transactions of a user or a specific wallet for a certain period in a convenient paginated form.

## Installation
1. Clone the repository:
    ```sh
    git clone git@github.com:swwdev/expense-tracking-app.git
    cd expense-tracking-app
    ```

2. Install dependencies:
    ```sh
    ./mvnw package
    ```

3. Set up the database using Docker:
    - Ensure Docker is installed and running.
    - Navigate to the `./docker` directory.
    - Run the following command to start the database:
        ```sh
        cd ./docker
        docker-compose up -d
        ```

4. Update the `application.yml` file with your database or email credentials if necessary.

5. set up jdk 19

6. Run the application:
    ```sh
    java -jar target/expense-tracking-0.0.1-SNAPSHOT.jar
    ```

## Usage
- Access the application at `http://localhost:8080`.
- Use the API documentation available at `http://localhost:8080/swagger-ui.html` to explore the available endpoints and their usage.

## API Documentation
The API is documented using OpenAPI and can be accessed at:
`http://localhost:8080/swagger-ui.html`

## Testing
- Tested some application components using unit tests with mockito
- Integration tests are conducted using Testcontainers, which dynamically create and manage PostgreSQL containers during testing.
- The application has been tested using Postman. A Postman collection is serialized and included in `./postman` for your convenience.

## License
This project is licensed under the MIT License.
