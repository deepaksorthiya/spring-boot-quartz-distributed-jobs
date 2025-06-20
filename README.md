<h1 style="text-align: center;">pring Boot Quartz Distributed Jobs Starter Project</h1>

<p style="text-align: center;">
  <a href="https://github.com/deepaksorthiya/spring-boot-quartz-distributed-jobs/workflows/maven-build.yml">
    <img src="https://github.com/deepaksorthiya/spring-boot-quartz-distributed-jobs/actions/workflows/maven-build.yml/badge.svg" alt="Java Maven Build Test"/>
  </a>
  <a href="https://hub.docker.com/r/deepaksorthiya/spring-boot-quartz-distributed-jobs">
    <img src="https://img.shields.io/docker/pulls/deepaksorthiya/spring-boot-quartz-distributed-jobs" alt="Docker"/>
  </a>
  <a href="https://spring.io/projects/spring-boot">
    <img src="https://img.shields.io/badge/spring--boot-3.5.0-brightgreen?logo=springboot" alt="Spring Boot"/>
  </a>
</p>

## Live Demo

TBD

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Requirements](#-requirements)
- [Getting Started](#-getting-started)
    - [Clone the Repository](#1-clone-the-repository)
    - [Start Docker](#2-start-docker)
    - [Build the Project](#3-build-the-project)
    - [Run Project Locally](#4-run-the-project)
    - [Build Docker Image](#5-optional-build-docker-image-docker-should-be-running)
    - [Run Docker Image](#6-optional-running-on-docker)
    - [Deploy on Kubernetes with Helm](#7-optionalrun-on-local-minikube-kubernetes-using-helm-chart)
- [Testing](#-testing)
- [Clean Up](#-cleanup)
- [Reference Documentation](#-reference-documentation)
- [Guides](#-guides)

---

## 🚀 Overview

A sample project demonstrating how to schedule and manage distributed jobs
using [Quartz Scheduler](https://www.quartz-scheduler.org/) with [Spring Boot](https://spring.io/projects/spring-boot).
This project uses an H2 in-memory database and exposes Quartz management endpoints via Spring Boot Actuator.

---

## 🚀 Features

- **Quartz Scheduler Integration**: Schedule jobs using Quartz with JDBC job store.
- **Spring Boot Actuator**: Monitor and manage jobs and triggers via HTTP endpoints.
- **Sample Jobs & Triggers**: Pre-configured jobs and triggers for demonstration.
- **H2 Database**: In-memory database for easy setup and testing.
- **Virtual Threads**: Enabled for improved concurrency (Java 21+).

---

## 📦 Requirements

- Git `2.50.0+`
- Spring Boot `3.5.0`
- Java `24`
- Maven `3.9+`
- (Optional)Docker Desktop (tested on `4.42.0`)
- (Optional) Minikube/Helm for Kubernetes

---

## 🛠️ Getting Started

### 1. Clone the Repository

```sh
git clone https://github.com/deepaksorthiya/spring-boot-quartz-distributed-jobs.git
cd spring-boot-quartz-distributed-jobs
```

### 2. Start Docker

```sh
docker compose up
```

### 3. Build the Project

```sh
./mvnw clean package -DskipTests
```

OR

for native run

```bash
./mvnw native:compile -Pnative
```

### 4. Run the Project

```sh
./mvnw spring-boot:run
```

OR

```sh
java -jar .\target\spring-boot-quartz-distributed-jobs-0.0.1-SNAPSHOT.jar
```

OR

Run native image directly

```bash
target/spring-boot-quartz-distributed-jobs
```

### 5. (Optional) Build Docker Image (docker should be running)

```sh
./mvnw clean spring-boot:build-image -DskipTests
```

OR

To create the native container image, run the following goal:

```bash
./mvnw spring-boot:build-image -Pnative
```

### 6. (Optional) Running On Docker

```sh
docker run -p 8080:8080 --name spring-boot-quartz-distributed-jobs deepaksorthiya/spring-boot-quartz-distributed-jobs:latest
```

### 7. (Optional)Run on Local minikube Kubernetes using Helm Chart

```sh
cd helm
helm create spring-boot-quartz-distributed-jobs
helm lint spring-boot-quartz-distributed-jobs
helm install spring-boot-quartz-distributed-jobs spring-boot-quartz-distributed-jobs
helm uninstall spring-boot-quartz-distributed-jobs spring-boot-quartz-distributed-jobs
```

---

## 🧪 Testing

- Access the API: [http://localhost:8080](http://localhost:8080)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### 1. Quartz Actuator Endpoints

Spring Boot Actuator exposes the following endpoints for Quartz:

- ```/actuator/quartz``` — Overview of jobs and triggers
- ```/actuator/quartz/jobs``` — List job groups
- ```/actuator/quartz/jobs/{group}/{name}``` — Job details
- ```/actuator/quartz/triggers``` — List trigger groups
- ```/actuator/quartz/triggers/{group}/{name}``` — Trigger details
  You can also trigger jobs manually via HTTP POST.

### 2. Example Jobs & Triggers

Jobs are defined in [JobConfig](src/main/java/com/example/sample/JobConfig.java):

- helloJob (group: samples)
- anotherJob (group: samples)
- onDemandJob (group: samples)

Sample triggers include:

- Every 5 seconds
- Every day
- 3am on weekdays
- Once a week
- Every hour during working hours on Tuesday and Thursday
  See [JobConfig](src/main/java/com/example/sample/JobConfig.java) for details.

### 3. Run all tests

with:

```bash
./mvnw test
```

To run your existing tests in a native image, run the following goal:

```bash
./mvnw test -PnativeTest
```

Tests cover:

- Job scheduling and execution
- Actuator endpoint responses
- Manual job triggering

---

## 🧹 Cleanup

```sh
docker compose down -v
```

---

## 📚 Reference Documentation

For further reference, please consider the following sections:

- [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
- [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/maven-plugin)
- [Create an OCI image](https://docs.spring.io/spring-boot/maven-plugin/build-image.html)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/index.html)
- [Spring Web](https://docs.spring.io/spring-boot/reference/web/servlet.html)
- [Spring Data JPA](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data)
- [Validation](https://docs.spring.io/spring-boot//io/validation.html)
- [Flyway Migration](https://docs.spring.io/spring-boot/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)

---

## 📚 Guides

The following guides illustrate how to use some features concretely:

- [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
- [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Validation](https://spring.io/guides/gs/validating-form-input/)
- [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

---

<p style="text-align: center;">
  <b>Happy Coding!</b> 🚀
</p>
