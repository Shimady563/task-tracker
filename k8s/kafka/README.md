# Kafka Setup

## Предварительные требования

Перед применением манифестов Kafka необходимо установить Strimzi Operator в кластер.

### Установка Strimzi Operator

```bash
# Создать namespace для operator (можно использовать существующий namespace)
kubectl create namespace kafka-operator

# Установить Strimzi Operator
kubectl apply -f 'https://strimzi.io/install/latest?namespace=kafka-operator' -n kafka-operator

# Проверить, что operator установлен
kubectl get pods -n kafka-operator
```

### Применение манифестов

```bash
# Применить Kafka cluster
kubectl apply -f kafka-cluster.yaml

# Дождаться готовности Kafka (может занять несколько минут)
kubectl wait kafka/kafka-cluster --for=condition=Ready --timeout=300s -n backend

# Применить топики
kubectl apply -f kafka-topics.yaml
```

### Проверка состояния

```bash
# Проверить состояние Kafka cluster
kubectl get kafka -n backend

# Проверить поды Kafka
kubectl get pods -n backend -l strimzi.io/kind=Kafka

# Проверить топики
kubectl get kafkatopic -n backend

# Получить bootstrap servers для подключения
kubectl get kafka kafka-cluster -n backend -o jsonpath='{.status.listeners[?(@.type=="plain")].bootstrapServers}'
```

### Использование Kafka в сервисах

Для подключения к Kafka из Spring Boot приложений используйте:
- Bootstrap servers: `kafka-cluster-kafka-bootstrap.backend.svc:9092`
- Или: `kafka-cluster-kafka-brokers.backend.svc.cluster.local:9092`


