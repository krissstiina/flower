# Flower Shop - Инструкция по запуску

> Обновлено для демонстрации CI/CD pipeline

## Запуск всех сервисов

В корневой папке проекта выполните:

```bash
docker-compose up --build -d
```

Эта команда:
- Соберет Docker образы для всех сервисов
- Запустит все контейнеры в фоновом режиме
- Создаст необходимые сети и volumes

## Просмотр логов

Для просмотра логов всех сервисов:

```bash
docker-compose logs -f
```

Для просмотра логов конкретного сервиса:

```bash
docker-compose logs -f demo-rest
docker-compose logs -f notification-service
docker-compose logs -f audit-service
docker-compose logs -f analytics-service
```

## Тестирование API

### POST запрос для рейтинга букета

Используйте Postman или curl для отправки запроса:

**POST** `http://localhost:8080/api/bouquets/{id}/rate`

Пример:
```bash
curl -X POST http://localhost:8080/api/bouquets/1/rate
```

Или через Postman:
- Method: `POST`
- URL: `http://localhost:8080/api/bouquets/1/rate`
- Headers: `Content-Type: application/json`

Этот запрос:
1. Вызовет gRPC сервис analytics-service для расчета популярности
2. Отправит событие в RabbitMQ Fanout exchange
3. Создаст трейс для отслеживания в Zipkin

## Мониторинг и трейсинг

### Zipkin - Просмотр трейсов

Откройте в браузере:
```
http://localhost:9411
```

**Как использовать:**
1. Откройте http://localhost:9411
2. Нажмите кнопку **"Run Query"** (или просто подождите, если трейсы уже есть)
3. Вы увидите все трейсы запросов между сервисами
4. Кликните на трейс, чтобы увидеть детали

### Prometheus - Метрики

Откройте в браузере:
```
http://localhost:9090
```

**Полезные запросы:**
- `http_server_requests_seconds_count` - количество HTTP запросов
- `jvm_memory_used_bytes` - использование памяти JVM
- `process_cpu_usage` - использование CPU

### Actuator Endpoints

Для каждого сервиса доступны Actuator endpoints:

- **Demo REST** (8080): http://localhost:8080/actuator
  - Health: http://localhost:8080/actuator/health
  - Prometheus: http://localhost:8080/actuator/prometheus
  - Metrics: http://localhost:8080/actuator/metrics

- **Notification Service** (8083): http://localhost:8083/actuator
  - Health: http://localhost:8083/actuator/health
  - Prometheus: http://localhost:8083/actuator/prometheus

- **Audit Service** (8082): http://localhost:8082/actuator
  - Health: http://localhost:8082/actuator/health
  - Prometheus: http://localhost:8082/actuator/prometheus

- **Analytics Service** (8081): http://localhost:8081/actuator
  - Health: http://localhost:8081/actuator/health
  - Prometheus: http://localhost:8081/actuator/prometheus

### RabbitMQ Management

Откройте в браузере:
```
http://localhost:15672
```

**Учетные данные:**
- Username: `guest`
- Password: `guest`

Здесь вы можете:
- Просматривать очереди и обменники
- Мониторить сообщения
- Управлять соединениями

## Остановка сервисов

Для остановки всех сервисов:

```bash
docker-compose down
```

Для остановки с удалением volumes (удалит данные БД):

```bash
docker-compose down -v
```

## Полезные команды

Проверка статуса контейнеров:
```bash
docker-compose ps
```

Перезапуск конкретного сервиса:
```bash
docker-compose restart demo-rest
```

Просмотр использования ресурсов:
```bash
docker stats
```

## Структура сервисов

- **demo-rest** (8080) - Основной REST API сервис
- **notification-service** (8083) - Сервис уведомлений через WebSocket
- **audit-service** (8082) - Сервис аудита событий
- **analytics-service** (8081 HTTP, 9090 gRPC) - gRPC сервис аналитики

## Порты

- 8080 - Demo REST API
- 8081 - Analytics Service (HTTP/Actuator)
- 8082 - Audit Service
- 8083 - Notification Service
- 9090 - Analytics Service (gRPC)
- 9091 - Prometheus (внешний доступ, внутренний порт 9090)
- 9411 - Zipkin
- 5433 - PostgreSQL (внешний доступ)
- 5672 - RabbitMQ
- 15672 - RabbitMQ Management UI

