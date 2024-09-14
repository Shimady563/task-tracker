# Task Tracker app

## Overview

A microservice application of a task tracker.
The core features of the application are:

- Account creation for better task management
- Ability to edit personal information
- Task creation, update, and deletion
- Various notifications to help user keep track of tasks and boost productivity

Additional technical features include data validation,
error handling and protection against common security threats.
Github actions for running tests are triggered for every service on every commit and push
and for publishing images to docker hub after test workflow in master and dev branches.
Also, application is easy to deploy and run locally with docker-compose.

## How to run

The main requirement is to have Docker installed on your local machine.
if you don't have it installed, you can do it here: https://docs.docker.com/get-docker/

Next, clone repository to your local machine

```bash
git clone https://github.com/Shimady563/task-tracker.git
```

Go to the cloned project directory and create .evn file 
for email server configuration (you can use mailtrap.io)

Examaple of .env file

```bash
MAIL_USERNAME=<your_username>
MAIL_PASSWORD=<your_password>
MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
```

Then you can run the application

```bash
docker compose -f ./production-compose.yaml up  -d
```

You can view documentation at http://localhost:8080/swagger-ui/index.html

Kafka ui is available at http://localhost:8090

To stop the application run

```bash
docker compose -f ./production-compose.yaml down
```

## Technologies used

- Application is based on Spring and Spring Boot
- Data storage is managed by PostgreSQL
- Security is handled by Spring Security
- Communication between services is done with Kafka
- Application is built and deployed with Docker
- DB schema migration is handled with Liquibase

The validation, error handling, testing, mailing are implemented using Spring Boot features.
Notification types include email, sms, and push notifications.

## Architecture

The application is built with microservices architecture. 
The main services include tracker service which is capable of managing tasks and users, 
and notification service which is responsible for sending notifications to users. 
The services communicate with each other using Kafka.
It is easy to add functionality to the application by 
adding new services due to high modularity of architecture.

## Future plans

Now first version of the application is completed,
and it has every feature that was planned.
I plan to add k8s cluster configuration
for easy scalable deployment.

## API description

**Note: before you access any of the endpoints,
you should create an account and login into it. 
In swagger docs use login endpoint to login into account
instead of swagger's default login**

You can view documentation at 

http://localhost:8080/swagger-ui/index.html

Additionally, you can view the documentation in Postman format at

https://documenter.getpostman.com/view/33505917/2sAXjM2r3y