# Flower Shop 


### Prometheus - Метрики

```
http://localhost:9090
```

**Полезные запросы:**
- `http_server_requests_seconds_count` - количество HTTP запросов
- `jvm_memory_used_bytes` - использование памяти JVM
- `process_cpu_usage` - использование CPU

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


                    // Параллельная сборка Docker образов с использованием кеша
                    parallel(
                        'demo-rest': {
                            sh 'docker compose build demo-rest'
                        },
                        'analytics': {
                            sh 'docker compose build analytics-service'
                        },
                        'audit': {
                            sh 'docker compose build audit-service'
                        },
                        'notification': {
                            sh 'docker compose build notification-service'
                        }
                    )
                