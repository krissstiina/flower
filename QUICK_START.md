# ⚡ Быстрый старт для зачета

## 🚀 За 5 минут до зачета

### 1. Запуск всего
```bash
cd /Users/kristina/Desktop/analytic
docker-compose up --build -d
```

### 2. Проверка
```bash
docker-compose ps
curl http://localhost:8080/actuator/health
```

### 3. Jenkins
- Откройте: http://localhost:8085
- Pipeline: `flower-shop-pipeline` → Build Now

### 4. Метрики
- Prometheus: http://localhost:9091
- Grafana: http://localhost:3000 (admin/admin)
- Zipkin: http://localhost:9411

---

## 📝 Что говорить на зачете

### Демонстрация CI/CD:
1. "Сейчас я запущу docker-compose, который поднимет все сервисы"
2. "Jenkins настроен на автоматическую сборку при push в Git"
3. "Сделаю небольшое изменение и запушу в Git"
4. "Jenkins автоматически обнаружит изменения и запустит сборку"
5. "Pipeline проходит 4 стадии: Checkout, Build, Docker Compose, Health Check"

### Демонстрация метрик:
1. "Prometheus собирает метрики с Actuator endpoints"
2. "Grafana визуализирует метрики из Prometheus"
3. "Zipkin показывает трейсы распределенных запросов"
4. "Все метрики доступны через /actuator/prometheus"

---

## 🔗 Ссылки

- Jenkins: http://localhost:8085
- Prometheus: http://localhost:9091
- Grafana: http://localhost:3000
- Zipkin: http://localhost:9411
- Demo REST: http://localhost:8080
- Actuator: http://localhost:8080/actuator

---

## ⚠️ Если что-то не работает

```bash
# Перезапуск
docker-compose restart

# Логи
docker-compose logs -f

# Очистка и перезапуск
docker-compose down
docker-compose up --build -d
```




