# Структура Kubernetes манифестов

## Обзор

Все манифесты организованы по компонентам и логически сгруппированы в директории.

```
k8s/
├── namespace.yaml              # Namespace для всех ресурсов
│
├── postgres/                   # PostgreSQL база данных
│   ├── secret.yaml            # Credentials (username, password, database)
│   ├── service.yaml           # ClusterIP service на порту 5432
│   └── statefulset.yaml       # StatefulSet с persistent storage
│
├── redis/                      # Redis кэш
│   ├── secret.yaml            # Redis password
│   ├── service.yaml           # ClusterIP service на порту 6379
│   └── deployment.yaml        # Deployment для Redis
│
├── kafka/                      # Kafka cluster (Strimzi)
│   ├── kafka-cluster.yaml     # Kafka cluster с 3 брокерами и Zookeeper
│   ├── kafka-topics.yaml      # Kafka topics (email, sms, push, reminder)
│   └── README.md              # Инструкции по установке Strimzi
│
├── auth/                       # Auth микросервис
│   ├── configmap.yaml         # Не-секретные конфигурации
│   ├── secret.yaml            # Секреты (DB credentials, JWT secrets, Redis password)
│   ├── deployment.yaml        # Deployment с health checks и resources
│   ├── service.yaml           # ClusterIP service
│   └── hpa.yaml               # HorizontalPodAutoscaler (CPU-based)
│
├── tracker/                    # Tracker микросервис (API Gateway)
│   ├── configmap.yaml         # Не-секретные конфигурации
│   ├── secret.yaml            # Секреты (DB credentials, JWT secrets)
│   ├── deployment.yaml        # Deployment с health checks и resources
│   ├── service.yaml           # ClusterIP service
│   └── hpa.yaml               # HorizontalPodAutoscaler (CPU-based)
│
├── notification/               # Notification микросервис
│   ├── configmap.yaml         # Не-секретные конфигурации
│   ├── secret.yaml            # Секреты (SMTP credentials)
│   ├── deployment.yaml        # Deployment с health checks и resources
│   ├── service.yaml           # ClusterIP service
│   └── hpa.yaml               # HorizontalPodAutoscaler (CPU-based)
│
├── ingress/                    # Ingress для внешнего доступа
│   └── ingress.yaml           # NGINX ingress для API
│
├── README.md                   # Полная документация
├── DEPLOYMENT_ORDER.md         # Порядок деплоя и зависимости
├── STRUCTURE.md                # Этот файл
└── apply-all.sh                # Скрипт для быстрого применения всех манифестов
```

## Ключевые особенности

### Безопасность
- ✅ Все секреты вынесены в отдельные Secret ресурсы
- ✅ ConfigMap для не-секретных данных
- ✅ Security contexts в deployment (non-root, drop capabilities)
- ✅ Отдельные Secret для каждого сервиса

### Надежность
- ✅ Health checks (liveness и readiness probes)
- ✅ Resource limits и requests
- ✅ HPA для автоматического масштабирования
- ✅ Persistent storage для PostgreSQL и Kafka

### Масштабируемость
- ✅ HPA настроен для всех микросервисов (2-10 реплик)
- ✅ Kafka cluster с 3 брокерами для высокой доступности
- ✅ Replication factor 3 для Kafka topics

### Мониторинг
- ✅ Health endpoints через Spring Boot Actuator
- ✅ Готовность к интеграции с Prometheus (actuator endpoints)

## Порты сервисов

| Сервис | Внутренний порт | Service порт | Описание |
|--------|----------------|--------------|----------|
| PostgreSQL | 5432 | 5432 | База данных |
| Redis | 6379 | 6379 | Кэш |
| Kafka | 9092 | 9092 | Брокер сообщений |
| Auth | 8080 | 80 | Auth API |
| Tracker | 8080 | 80 | Main API Gateway |
| Notification | 8080 | 80 | Notification API |

## Ресурсы

### Минимальные требования на под
- PostgreSQL: 256Mi-512Mi RAM, 250m-500m CPU
- Redis: 128Mi-256Mi RAM, 100m-200m CPU
- Kafka (per broker): 2Gi-4Gi RAM, 1000m-2000m CPU
- Zookeeper (per replica): 512Mi-1Gi RAM, 250m-500m CPU
- Auth/Tracker/Notification: 512Mi-1Gi RAM, 250m-500m CPU (per pod)

### Storage
- PostgreSQL: 20Gi (per replica)
- Kafka: 100Gi (per broker)
- Zookeeper: 20Gi (per replica)

## Зависимости

```
┌─────────────┐
│ PostgreSQL  │──────────┐
└─────────────┘          │
                         ├──> Auth Service
┌─────────────┐          │
│    Redis    │──────────┘
└─────────────┘

┌─────────────┐
│ PostgreSQL  │──────────> Tracker Service
└─────────────┘

┌─────────────┐
│    Kafka    │──────────┬──> Auth Service
└─────────────┘          ├──> Tracker Service
                         └──> Notification Service
```

## Важные замечания

1. **Секреты**: Все секреты в YAML файлах содержат примеры значений. В production используйте внешние secret managers или kubectl create secret.

2. **Docker образы**: Замените `your-registry/*:latest` на реальные образы ваших сервисов.

3. **Домены**: Замените `api.example.com` в ingress.yaml на ваш реальный домен.

4. **Strimzi Operator**: Kafka требует предварительной установки Strimzi Operator.

5. **NGINX Ingress Controller**: Ingress требует предварительной установки NGINX Ingress Controller.

6. **Health Checks**: Убедитесь, что в Spring Boot приложениях включены liveness и readiness endpoints.

7. **TLS/SSL**: Для production рекомендуется настроить TLS/SSL в Ingress.


