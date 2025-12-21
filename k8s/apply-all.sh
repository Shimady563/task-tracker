#!/bin/bash

set -e

echo "=== Применение Kubernetes манифестов для Task Tracker ==="

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

check_status() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓${NC} $1"
    else
        echo -e "${RED}✗${NC} $1"
        exit 1
    fi
}

# namespace
echo ""
echo "1. Создание namespace..."
kubectl apply -f namespace.yaml
check_status "Namespace создан"

# Postgres
echo ""
echo "2. Деплой PostgreSQL..."
kubectl apply -f postgres/secret.yaml
kubectl apply -f postgres/service.yaml
kubectl apply -f postgres/statefulset.yaml
check_status "PostgreSQL развернут"

# Redis
echo ""
echo "3. Деплой Redis..."
kubectl apply -f redis/secret.yaml
kubectl apply -f redis/service.yaml
kubectl apply -f redis/statefulset.yaml
check_status "Redis развернут"

# Kafka
echo ""
echo "3. Деплой Kafka..."
kubectl apply -f kafka/secret.yaml
kubectl apply -f kafka/service.yaml
kubectl apply -f kafka/statefulset.yaml
check_status "Kafka развернута"

# Подождать готовности баз данных
echo ""
echo "5. Ожидание готовности баз данных..."
kubectl wait --for=condition=ready pod -l app=postgres -n tracker --timeout=300s || true
kubectl wait --for=condition=ready pod -l app=redis -n tracker --timeout=300s || true
kubectl wait --for=condition=ready pod -l app=kafka -n tracker --timeout=300s || true

# Auth
echo ""
echo "6. Деплой auth сервиса..."
kubectl apply -f auth/configmap.yaml
kubectl apply -f auth/secret.yaml
kubectl apply -f auth/service.yaml
kubectl apply -f auth/deployment.yaml
check_status "Auth сервис развернут"

# Tracker
echo ""
echo "7. Деплой tracker сервиса..."
kubectl apply -f tracker/configmap.yaml
kubectl apply -f tracker/secret.yaml
kubectl apply -f tracker/service.yaml
kubectl apply -f tracker/deployment.yaml
check_status "Tracker сервис развернут"

# Notification
echo ""
echo "8. Деплой notification сервиса..."
kubectl apply -f notification/configmap.yaml
kubectl apply -f notification/secret.yaml
kubectl apply -f notification/service.yaml
kubectl apply -f notification/deployment.yaml
check_status "Notification сервис развернут"

# Frontend
echo ""
echo "8. Деплой frontend..."
kubectl apply -f frontend/configmap.yaml
kubectl apply -f frontend/service.yaml
kubectl apply -f frontend/deployment.yaml
check_status "Frontend развернут"

echo "Деплой завершен"