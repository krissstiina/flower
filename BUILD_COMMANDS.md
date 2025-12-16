# Команды для сборки проектов

## Вариант 1: Автоматическая сборка (рекомендуется)

Используйте скрипт `build.sh`:

```bash
./build.sh
```

## Вариант 2: Ручная сборка

### Шаг 1: Сборка контрактов

```bash
# Сборка flower-events-contract
cd flower-events-contract
chmod +x mvnw
./mvnw clean install -DskipTests
cd ..

# Сборка flower-api-contract
cd flower-api-contract
chmod +x mvnw
./mvnw clean install -DskipTests
cd ..
```

### Шаг 2: Сборка сервисов

```bash
# Сборка demo-rest-flower
cd demo-rest-flower
chmod +x mvnw
./mvnw clean package -DskipTests
cd ..

# Сборка flower-analytics-service
cd flower-analytics-service
chmod +x mvnw
./mvnw clean package -DskipTests
cd ..

# Сборка flower-audit-service
cd flower-audit-service
chmod +x mvnw
./mvnw clean package -DskipTests
cd ..

# Сборка notification-service
cd notification-service
chmod +x mvnw
./mvnw clean package -DskipTests
cd ..
```

## Вариант 3: Одной командой (для копирования)

```bash
# Контракты
cd flower-events-contract && chmod +x mvnw && ./mvnw clean install -DskipTests && cd .. && \
cd flower-api-contract && chmod +x mvnw && ./mvnw clean install -DskipTests && cd .. && \
# Сервисы
cd demo-rest-flower && chmod +x mvnw && ./mvnw clean package -DskipTests && cd .. && \
cd flower-analytics-service && chmod +x mvnw && ./mvnw clean package -DskipTests && cd .. && \
cd flower-audit-service && chmod +x mvnw && ./mvnw clean package -DskipTests && cd .. && \
cd notification-service && chmod +x mvnw && ./mvnw clean package -DskipTests && cd ..
```

## После сборки

Запустите Docker Compose:

```bash
docker-compose up -d
```

## Примечания

- **Maven Wrapper (mvnw)** - не требует установки Maven на компьютере
- **-DskipTests** - пропускает тесты для ускорения сборки
- **clean** - очищает предыдущие сборки
- **install** - для контрактов (устанавливает в локальный репозиторий Maven)
- **package** - для сервисов (создает JAR файлы)

## Проверка сборки

Убедитесь, что JAR файлы созданы:

```bash
ls -la demo-rest-flower/target/*.jar
ls -la flower-analytics-service/target/*.jar
ls -la flower-audit-service/target/*.jar
ls -la notification-service/target/*.jar
```

