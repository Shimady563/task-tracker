# Порядок деплоя Kubernetes манифестов

Этот документ описывает правильный порядок применения манифестов и зависимости между компонентами.

## Зависимости между компонентами

```
PostgreSQL ─┐
            ├─> Auth Service
Redis ──────┘

PostgreSQL ──> Tracker Service

Kafka ────────> Auth Service
            └─> Tracker Service
            └─> Notification Service

Auth Service ──> Tracker Service (для проверки JWT токенов)

Tracker Service ──> Notification Service (через Kafka)
```

## Рекомендуемый порядок деплоя

### Этап 1: Инфраструктура (базы данных и брокеры сообщений)

1. **Namespace**
   ```bash
   kubectl apply -f namespace.yaml
   ```

2. **PostgreSQL** (StatefulSet, требует время на инициализацию)
   ```bash
   kubectl apply -f postgres/secret.yaml
   kubectl apply -f postgres/service.yaml
   kubectl apply -f postgres/statefulset.yaml
   
   # Дождаться готовности
   kubectl wait --for=condition=ready pod -l app=postgres -n backend --timeout=300s
   ```

3. **Redis**
   ```bash
   kubectl apply -f redis/secret.yaml
   kubectl apply -f redis/service.yaml
   kubectl apply -f redis/deployment.yaml
   
   # Дождаться готовности
   kubectl wait --for=condition=ready pod -l app=redis -n backend --timeout=300s
   ```

4. **Kafka** (требует Strimzi Operator, может занять 5-10 минут)
   ```bash
   # Проверить наличие Strimzi Operator
   kubectl get crd kafkas.kafka.strimzi.io
   
   # Если оператор установлен, применить Kafka
   kubectl apply -f kafka/kafka-cluster.yaml
   
   # Дождаться готовности (это может занять 5-10 минут!)
   kubectl wait kafka/kafka-cluster --for=condition=Ready --timeout=600s -n backend
   
   # Применить топики
   kubectl apply -f kafka/kafka-topics.yaml
   ```

### Этап 2: Микросервисы

5. **Auth Service** (зависит от PostgreSQL, Redis, Kafka)
   ```bash
   kubectl apply -f auth/configmap.yaml
   kubectl apply -f auth/secret.yaml
   kubectl apply -f auth/service.yaml
   kubectl apply -f auth/deployment.yaml
   kubectl apply -f auth/hpa.yaml
   ```

6. **Tracker Service** (зависит от PostgreSQL, Kafka, Auth для валидации токенов)
   ```bash
   kubectl apply -f tracker/configmap.yaml
   kubectl apply -f tracker/secret.yaml
   kubectl apply -f tracker/service.yaml
   kubectl apply -f tracker/deployment.yaml
   kubectl apply -f tracker/hpa.yaml
   ```

7. **Notification Service** (зависит от Kafka)
   ```bash
   kubectl apply -f notification/configmap.yaml
   kubectl apply -f notification/secret.yaml
   kubectl apply -f notification/service.yaml
   kubectl apply -f notification/deployment.yaml
   kubectl apply -f notification/hpa.yaml
   ```

### Этап 3: Сетевой доступ

8. **Ingress** (для внешнего доступа к API)
   ```bash
   # ВАЖНО: Отредактируйте ingress.yaml и замените api.example.com на ваш домен
   kubectl apply -f ingress/ingress.yaml
   ```

## Проверка деплоя

После применения всех манифестов проверьте статус:

```bash
# Проверить все поды
kubectl get pods -n backend

# Проверить сервисы
kubectl get svc -n backend

# Проверить ingress
kubectl get ingress -n backend

# Проверить HPA
kubectl get hpa -n backend

# Проверить логи
kubectl logs -f deployment/auth -n backend
kubectl logs -f deployment/tracker -n backend
kubectl logs -f deployment/notification -n backend
```

## Откат (Rollback)

Если что-то пошло не так, можно откатить деплойменты:

```bash
# Откатить deployment
kubectl rollout undo deployment/auth -n backend
kubectl rollout undo deployment/tracker -n backend
kubectl rollout undo deployment/notification -n backend

# Или удалить все ресурсы
kubectl delete namespace backend
```

## Быстрое применение

Для быстрого применения всех манифестов можно использовать скрипт:

```bash
chmod +x apply-all.sh
./apply-all.sh
```

Или применять по группам:

```bash
# Вся инфраструктура
kubectl apply -f namespace.yaml
kubectl apply -f postgres/
kubectl apply -f redis/
kubectl apply -f kafka/

# Все микросервисы
kubectl apply -f auth/
kubectl apply -f tracker/
kubectl apply -f notification/

# Ingress
kubectl apply -f ingress/
```


