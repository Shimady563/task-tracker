# Kubernetes Manifests для Task Tracker Backend

Полный набор Kubernetes манифестов для деплоя микросервисного Spring Boot приложения.

## Структура

```
k8s/
├── namespace.yaml           # Namespace backend
├── postgres/                # PostgreSQL база данных
│   ├── secret.yaml
│   ├── pvc.yaml
│   ├── service.yaml
│   └── statefulset.yaml
├── redis/                   # Redis кэш
│   ├── secret.yaml
│   ├── service.yaml
│   └── deployment.yaml
├── kafka/                   # Kafka cluster (Strimzi)
│   ├── kafka-cluster.yaml
│   ├── kafka-topics.yaml
│   └── README.md
├── auth/                    # Auth микросервис
│   ├── configmap.yaml
│   ├── secret.yaml
│   ├── deployment.yaml
│   ├── service.yaml
│   └── hpa.yaml
├── tracker/                 # Tracker микросервис (API Gateway)
│   ├── configmap.yaml
│   ├── secret.yaml
│   ├── deployment.yaml
│   ├── service.yaml
│   └── hpa.yaml
├── notification/            # Notification микросервис
│   ├── configmap.yaml
│   ├── secret.yaml
│   ├── deployment.yaml
│   ├── service.yaml
│   └── hpa.yaml
├── ingress/                 # Ingress для внешнего доступа
│   └── ingress.yaml
└── README.md                # Этот файл
```

## Предварительные требования

### 1. Kubernetes кластер

Минимальные требования:
- Kubernetes 1.24+
- Минимум 4 CPU cores и 8GB RAM (для всех компонентов)
- Storage класс для PersistentVolumes

### 2. Установка Strimzi Operator (для Kafka)

```bash
# Создать namespace для operator
kubectl create namespace kafka-operator

# Установить Strimzi Operator
kubectl apply -f 'https://strimzi.io/install/latest?namespace=kafka-operator' -n kafka-operator

# Проверить установку
kubectl get pods -n kafka-operator
```

### 3. Установка NGINX Ingress Controller

```bash
# Установить NGINX Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.1/deploy/static/provider/cloud/deploy.yaml

# Проверить установку
kubectl get pods -n ingress-nginx
```

## Порядок деплоя

### Шаг 1: Создать namespace

```bash
kubectl apply -f namespace.yaml
```

### Шаг 2: Деплой инфраструктуры

#### PostgreSQL

```bash
kubectl apply -f postgres/secret.yaml
kubectl apply -f postgres/pvc.yaml
kubectl apply -f postgres/service.yaml
kubectl apply -f postgres/statefulset.yaml

# Проверить статус
kubectl get pods -n backend -l app=postgres
```

#### Redis

```bash
kubectl apply -f redis/secret.yaml
kubectl apply -f redis/service.yaml
kubectl apply -f redis/deployment.yaml

# Проверить статус
kubectl get pods -n backend -l app=redis
```

#### Kafka

```bash
# Применить Kafka cluster (может занять несколько минут)
kubectl apply -f kafka/kafka-cluster.yaml

# Дождаться готовности
kubectl wait kafka/kafka-cluster --for=condition=Ready --timeout=600s -n backend

# Применить топики
kubectl apply -f kafka/kafka-topics.yaml

# Проверить статус
kubectl get pods -n backend -l strimzi.io/kind=Kafka
kubectl get kafkatopic -n backend
```

### Шаг 3: Деплой микросервисов

#### Auth сервис

```bash
kubectl apply -f auth/configmap.yaml
kubectl apply -f auth/secret.yaml
kubectl apply -f auth/service.yaml
kubectl apply -f auth/deployment.yaml
kubectl apply -f auth/hpa.yaml

# Проверить статус
kubectl get pods -n backend -l app=auth
```

#### Tracker сервис

```bash
kubectl apply -f tracker/configmap.yaml
kubectl apply -f tracker/secret.yaml
kubectl apply -f tracker/service.yaml
kubectl apply -f tracker/deployment.yaml
kubectl apply -f tracker/hpa.yaml

# Проверить статус
kubectl get pods -n backend -l app=tracker
```

#### Notification сервис

```bash
kubectl apply -f notification/configmap.yaml
kubectl apply -f notification/secret.yaml
kubectl apply -f notification/service.yaml
kubectl apply -f notification/deployment.yaml
kubectl apply -f notification/hpa.yaml

# Проверить статус
kubectl get pods -n backend -l app=notification
```

### Шаг 4: Настройка Ingress

```bash
# Отредактируйте ingress.yaml и замените api.example.com на ваш домен
kubectl apply -f ingress/ingress.yaml

# Проверить статус
kubectl get ingress -n backend
```

## Настройка перед деплоем

### 1. Обновить Docker образы

В каждом `deployment.yaml` замените `your-registry/*:latest` на ваши реальные образы:

```yaml
image: your-registry/auth:latest  # Замените на ваш образ
```

### 2. Обновить Secrets

**ВАЖНО**: В production окружении НИКОГДА не храните секреты в YAML файлах!

Используйте один из следующих методов:

#### Метод 1: kubectl create secret

```bash
# PostgreSQL
kubectl create secret generic postgres-secret \
  --from-literal=username=postgres \
  --from-literal=password=YOUR_SECURE_PASSWORD \
  --from-literal=database=tracker \
  --namespace=backend

# Redis
kubectl create secret generic redis-secret \
  --from-literal=password=YOUR_SECURE_PASSWORD \
  --namespace=backend

# Auth
kubectl create secret generic auth-secret \
  --from-literal=db-username=postgres \
  --from-literal=db-password=YOUR_PASSWORD \
  --from-literal=redis-password=YOUR_REDIS_PASSWORD \
  --from-literal=jwt-access-secret=YOUR_ACCESS_SECRET \
  --from-literal=jwt-refresh-secret=YOUR_REFRESH_SECRET \
  --namespace=backend

# Tracker
kubectl create secret generic tracker-secret \
  --from-literal=db-username=postgres \
  --from-literal=db-password=YOUR_PASSWORD \
  --from-literal=jwt-access-secret=YOUR_ACCESS_SECRET \
  --namespace=backend

# Notification
kubectl create secret generic notification-secret \
  --from-literal=mail-username=your-email@example.com \
  --from-literal=mail-password=YOUR_EMAIL_PASSWORD \
  --from-literal=mail-host=smtp.example.com \
  --from-literal=mail-port=587 \
  --namespace=backend
```

#### Метод 2: External Secret Operator

Используйте External Secrets Operator для интеграции с AWS Secrets Manager, HashiCorp Vault и т.д.

### 3. Настроить домены в Ingress

Отредактируйте `ingress/ingress.yaml` и замените `api.example.com` на ваш реальный домен.

### 4. Настроить TLS/SSL (опционально, но рекомендуется)

Для production окружения настройте TLS:

```yaml
# В ingress.yaml раскомментируйте:
tls:
  - hosts:
      - api.example.com
    secretName: backend-tls-secret

# Создайте TLS secret:
kubectl create secret tls backend-tls-secret \
  --cert=path/to/cert.crt \
  --key=path/to/cert.key \
  --namespace=backend

# Или используйте cert-manager для автоматической генерации Let's Encrypt сертификатов
```

## Проверка деплоя

### Проверить статус всех подов

```bash
kubectl get pods -n backend
```

### Проверить логи сервисов

```bash
# Auth
kubectl logs -f deployment/auth -n backend

# Tracker
kubectl logs -f deployment/tracker -n backend

# Notification
kubectl logs -f deployment/notification -n backend
```

### Проверить HPA

```bash
kubectl get hpa -n backend
```

### Проверить сервисы

```bash
kubectl get svc -n backend
```

### Проверить Ingress

```bash
kubectl get ingress -n backend
kubectl describe ingress backend-ingress -n backend
```

### Проверить health endpoints

```bash
# Tracker
kubectl port-forward svc/tracker 8080:80 -n backend
curl http://localhost:8080/actuator/health

# Auth
kubectl port-forward svc/auth 8081:80 -n backend
curl http://localhost:8081/actuator/health
```

## Мониторинг и логирование

Рекомендуется добавить:

1. **Prometheus** для метрик
2. **Grafana** для визуализации
3. **ELK Stack** или **Loki** для централизованного логирования
4. **Jaeger** или **Zipkin** для distributed tracing

## Troubleshooting

### Поды не запускаются

```bash
# Проверить события
kubectl get events -n backend --sort-by='.lastTimestamp'

# Проверить описание пода
kubectl describe pod <pod-name> -n backend

# Проверить логи
kubectl logs <pod-name> -n backend
```

### Проблемы с подключением к базе данных

```bash
# Проверить состояние PostgreSQL
kubectl get pods -n backend -l app=postgres
kubectl logs -f statefulset/postgres -n backend

# Проверить service
kubectl get svc postgres -n backend
kubectl describe svc postgres -n backend
```

### Проблемы с Kafka

```bash
# Проверить Kafka cluster
kubectl get kafka -n backend
kubectl describe kafka kafka-cluster -n backend

# Проверить поды Kafka
kubectl get pods -n backend -l strimzi.io/kind=Kafka

# Получить bootstrap servers
kubectl get kafka kafka-cluster -n backend -o jsonpath='{.status.listeners[?(@.type=="plain")].bootstrapServers}'
```

### Проблемы с Ingress

```bash
# Проверить ingress controller
kubectl get pods -n ingress-nginx

# Проверить ingress
kubectl describe ingress backend-ingress -n backend

# Проверить nginx ingress controller logs
kubectl logs -n ingress-nginx -l app.kubernetes.io/component=controller
```

## Масштабирование

Все микросервисы настроены с HPA и будут автоматически масштабироваться на основе CPU использования.

Для ручного масштабирования:

```bash
kubectl scale deployment/auth --replicas=5 -n backend
kubectl scale deployment/tracker --replicas=5 -n backend
kubectl scale deployment/notification --replicas=5 -n backend
```

## Backup

Рекомендуется настроить автоматические бэкапы для:

1. **PostgreSQL**: Используйте pg_dump или инструменты типа Velero
2. **Kafka**: Настройте репликацию топиков

## Обновление

### Обновление образа сервиса

```bash
# Обновить образ
kubectl set image deployment/auth auth=your-registry/auth:v2.0.0 -n backend

# Проверить статус rollout
kubectl rollout status deployment/auth -n backend

# Откатить изменения при необходимости
kubectl rollout undo deployment/auth -n backend
```

## Удаление

Для удаления всех ресурсов:

```bash
kubectl delete namespace backend
```

Или удалить компоненты по отдельности:

```bash
kubectl delete -f ingress/
kubectl delete -f notification/
kubectl delete -f tracker/
kubectl delete -f auth/
kubectl delete -f kafka/
kubectl delete -f redis/
kubectl delete -f postgres/
kubectl delete -f namespace.yaml
```

## Дополнительные ресурсы

- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Strimzi Documentation](https://strimzi.io/docs/)
- [NGINX Ingress Controller](https://kubernetes.github.io/ingress-nginx/)
- [Spring Boot Kubernetes Guide](https://spring.io/guides/gs/spring-boot-kubernetes/)


