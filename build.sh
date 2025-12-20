#!/bin/bash

# Build all services
./gradlew bootJar

if [ $? -ne 0 ]; then
    echo "Error while building services"
    exit 1
fi

# Build and start containers
docker compose -f build-compose.yaml up --build -d

if [ $? -ne 0 ]; then
    echo "Error while building docker images"
else
    echo "Services are running in background. Use 'docker compose logs' to view logs"
fi