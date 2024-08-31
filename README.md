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

Go to the cloned project directory and run the application

```bash
docker compose -f ./production-comose.yaml up  -d
```

To stop the application run

```bash
docker compose -f ./production-comose.yaml down
```

## Technologies used

- Application is based on Spring and Spring Boot
- Data storage is managed by PostgreSQL
- Security is handled by Spring Security
- Communication between services is done with Kafka
- Application is built and deployed with Docker

The validation, error handling, testing are implemented using Spring Boot features.

## Architecture

The application is built with microservices architecture. 
The main services include tracker service which is capable of managing tasks and users, 
and notification service which is responsible for sending notifications to users. 
The services communicate with each other using Kafka.
It is easy to add functionality to the application by 
adding new services due to high modularity of architecture.
At the moment the tracker service is fully completed 
and notification service under development.

## Future plans

Now that the first version of the application is completed, 
I'm working on the notification service. 
After all the services are completed,
I plan to add swagger documentation and 
configure kubernetes to operate the application 
for easy scalable deployment.

## API description

For now, before swagger documentation is added, 
I'm adding this postman API documentation instead.

**Note: before you access any of the endpoints, 
you should create an account and login into it**

https://documenter.getpostman.com/view/33505917/2sAXjM2r3y