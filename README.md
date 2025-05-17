This document is available in other languages: [Русский](README_ru.md)
# TransactionsSystem

An educational pet project for practical mastering of new programming skills and working with transactions.

## About the project

TransactionsSystem is a modular system for managing users, accounts, and transactions. The goal of the project is to consolidate knowledge in application architecture, REST and SOAP services, as well as testing.

During the development of the project, the following topics were explored:
- **Multithreading** – basics
- **Spring Framework** – annotations and integrations
- **Aspect-Oriented Programming (AOP)** – logging methods via AOP
- **Logger configuration** – modifying and adding loggers
- **Hibernate / Spring Data** – studying setup and configuration
- **Configuring custom cache providers; second-level cache providers; second-level cache implementations (EHCache); query cache setup**
- **Maven** – multi-module project configuration
- **Unit and integration testing**
- **Regular expressions**
- **Working with containers**
- **XML and SOAP**

## Technologies used

- Java 17 LTS version
- Maven
- Spring Boot 3.x (core, data, jpa, web)
- Hibernate 6.x
- PostgreSQL 14.1
- Flyway DB migration
- Swagger
- TestContainers
- JUnit 5
- Docker + Compose

- Modular architecture:
    - ts-accounts — user and account management
    - ts-common — common classes and DTOs
    - ts-transactions — creating and processing transactions
    - ts-reports — transaction reports

Database schema name is **transactions**.  
Database structure for all modules is created using Flyway migrations; only connection to the database at the addresses specified in the modules is required.

The project supports launching both from the IDE and via Docker containers.

---

## Module descriptions

### ts-accounts

This module is responsible for all operations related to users and their bank accounts, such as CRUD operations, data verification, and transaction execution.

- Database:  
  `jdbc:postgresql://localhost:5434/ts-accounts`  
  User/password: `postgres` / `pwpostgres`
- Swagger UI: `http://localhost:8089/swagger-ui/index.html`

Implemented features in this module:
- Minimal configuration of Hibernate / Spring Data
- Configured 2nd and 3rd level caches
- Regular expressions
- Logger configuration
- Aspect-Oriented Programming for service logging

### ts-transactions

This module is responsible for creating transactions between users’ bank accounts.

- Database:  
  `jdbc:postgresql://localhost:5435/ts-transactions`  
  User/password: `postgres` / `pwpostgres`
- Swagger UI: `http://localhost:8090/swagger-ui/index.html`
- SOAP Endpoint implemented (details below)

This module interacts via REST with the accounts module to retrieve data about user bank accounts involved in transactions.

Currently, some parts of the module’s logic are incomplete. For example, cash withdrawal handling and related issues and constraints are not yet implemented. Also, transaction status change is implemented in a minimal form.

In addition to usual CRUD operations, the module can generate many database records with random bank accounts and transaction data for testing the reports module. It also has a method to clear the transactions database.

The module implements a **SOAP endpoint** located at `http://localhost:8090/soap/`.  
This endpoint handles creating transactions between users. The XML request structure to this endpoint is as follows:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ts="http://tstransactions.klokov.ru/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <ts:SoapTransactionRequestDto>
         <ts:senderId>1</ts:senderId>
         <ts:recipientId>2</ts:recipientId>
         <ts:amount>10.0</ts:amount>
         <ts:typeId>1</ts:typeId>
      </ts:SoapTransactionRequestDto>
   </soapenv:Body>
</soapenv:Envelope>
```
### ts-reports

This module handles operations with reports. It allows retrieving reports from the transactions database, clearing the reports database, and searching reports via criteria-based queries.

- Database:
  - jdbc:postgresql://localhost:5436/ts-reports
  - User/password: postgres / pwpostgres

- Swagger UI: http://localhost:8091/swagger-ui/index.html

Also, this module implements two ways to get reports from the transactions module — single-threaded and multi-threaded.
To obtain data from the transactions module, the ReportsService provides:

fillAllOrNewReportsToDB() — single-threaded data retrieval and saving to DB

concurrentFillAllOrNewReportsToDB() — multi-threaded data retrieval and saving to DB

ts-reports
This module handles operations with reports. It allows retrieving reports from the transactions database, clearing the reports database, and searching reports via criteria-based queries.

- Database:
  - jdbc:postgresql://localhost:5436/ts-reports
  - User/password: postgres / pwpostgres
 - Swagger UI: http://localhost:8091/swagger-ui/index.html

Also, this module implements two ways to get reports from the transactions module — single-threaded and multi-threaded.
To obtain data from the transactions module, the ReportsService provides:

- fillAllOrNewReportsToDB() — single-threaded data retrieval and saving to DB

- concurrentFillAllOrNewReportsToDB() — multi-threaded data retrieval and saving to DB

ts-common
This module contains classes, entities, DTOs, and other components used by two or more modules of the application.

### ts-common

This module contains classes, entities, DTOs, and other components used by two or more modules of the application.

--- 
