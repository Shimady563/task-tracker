#!/bin/bash

# Скрипт для применения всех Kubernetes манифестов
# Использование: ./apply-all.sh

set -e  # Прервать выполнение при ошибке

echo "=== Применение Kubernetes манифестов для Task Tracker Backend ==="

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Функция для проверки успешного выполнения команды
check_status() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓${NC} $1"
    else
        echo -e "${RED}✗${NC} $1"
        exit 1
    fi
}

# Создать namespace
echo ""
echo "1. Создание namespace..."
kubectl apply -f namespace.yaml
check_status "Namespace создан"

# PostgreSQL
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
kubectl apply -f redis/deployment.yaml
check_status "Redis развернут"

# Kafka (требует Strimzi Operator)
echo ""
echo -e "${YELLOW}4. Деплой Kafka (требует установленный Strimzi Operator)...${NC}"
echo "Проверка наличия Strimzi Operator..."
if kubectl get crd kafkas.kafka.strimzi.io > /dev/null 2>&1; then
    kubectl apply -f kafka/kafka-cluster.yaml
    echo "Ожидание готовности Kafka cluster (это может занять несколько минут)..."
    kubectl wait kafka/kafka-cluster --for=condition=Ready --timeout=600s -n backend || echo "Kafka может еще инициализироваться..."
    kubectl apply -f kafka/kafka-topics.yaml
    check_status "Kafka развернут"
else
    echo -e "${YELLOW}Strimzi Operator не найден. Пропускаем Kafka.${NC}"
    echo "Для установки Strimzi Operator выполните:"
    echo "  kubectl create namespace kafka-operator"
    echo "  kubectl apply -f 'https://strimzi.io/install/latest?namespace=kafka-operator' -n kafka-operator"
fi

# Подождать готовности баз данных
echo ""
echo "5. Ожидание готовности баз данных..."
kubectl wait --for=condition=ready pod -l app=postgres -n backend --timeout=300s || true
kubectl wait --for=condition=ready pod -l app=redis -n backend --timeout=300s || true

# Auth сервис
echo ""
echo "6. Деплой auth сервиса..."
kubectl apply -f auth/configmap.yaml
kubectl apply -f auth/secret.yaml
kubectl apply -f auth/service.yaml
kubectl apply -f auth/deployment.yaml
kubectl apply -f auth/hpa.yaml
check_status "Auth сервис развернут"

# Tracker сервис
echo ""
echo "7. Деплой tracker сервиса..."
kubectl apply -f tracker/configmap.yaml
kubectl apply -f tracker/secret.yaml
kubectl apply -f tracker/service.yaml
kubectl apply -f tracker/deployment.yaml
kubectl apply -f tracker/hpa.yaml
check_status "Tracker сервис развернут"

# Notification сервис
echo ""
echo "8. Деплой notification сервиса..."
kubectl apply -f notification/configmap.yaml
kubectl apply -f notification/secret.yaml
kubectl apply -f notification/service.yaml
kubectl apply -f notification/deployment.yaml
kubectl apply -f notification/hpa.yaml
check_status "Notification сервис развернут"

# Ingress
echo ""
echo "9. Настройка Ingress..."
echo -e "${YELLOW}ВАЖНО: Перед применением отредактируйте ingress/ingress.yaml и замените api.example.com на ваш домен${NC}"
read -p "Применить Ingress? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    kubectl apply -f ingress/ingress.yaml
    check_status "Ingress настроен"
else
    echo "Ingress пропущен. Примените его позже вручную."
fi

echo ""
echo -e "${GREEN}=== Деплой завершен! ===${NC}"
echo ""
echo "Проверка статуса:"
echo "  kubectl get pods -n backend"
echo "  kubectl get svc -n backend"
echo "  kubectl get ingress -n backend"
echo ""
echo "Проверка логов:"
echo "  kubectl logs -f deployment/auth -n backend"
echo "  kubectl logs -f deployment/tracker -n backend"
echo "  kubectl logs -f deployment/notification -n backend"


