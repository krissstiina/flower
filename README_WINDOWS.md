# Развертывание проекта на Windows

## Требования

1. **Git** - для клонирования репозитория
   - Скачать: https://git-scm.com/download/win
   - Или использовать Git Bash (входит в установку Git)

2. **Docker Desktop для Windows**
   - Скачать: https://www.docker.com/products/docker-desktop
   - Убедитесь, что Docker Desktop запущен

3. **Java JDK 17 или 21** (опционально, если хотите использовать установленный Maven)
   - Maven Wrapper (mvnw) не требует установки Maven/Java, но Java нужна для запуска приложений
   - Скачать: https://adoptium.net/

## Клонирование проекта

```cmd
git clone https://github.com/krissstiina/flower.git
cd flower
```

## Сборка проектов

### Вариант 1: Автоматическая сборка (рекомендуется)

Используйте скрипт `build.bat`:

```cmd
build.bat
```

### Вариант 2: Ручная сборка

#### Шаг 1: Сборка контрактов

```cmd
cd flower-events-contract
mvnw.cmd clean install -DskipTests
cd ..

cd flower-api-contract
mvnw.cmd clean install -DskipTests
cd ..
```

#### Шаг 2: Сборка сервисов

```cmd
cd demo-rest-flower
mvnw.cmd clean package -DskipTests
cd ..

cd flower-analytics-service
mvnw.cmd clean package -DskipTests
cd ..

cd flower-audit-service
mvnw.cmd clean package -DskipTests
cd ..

cd notification-service
mvnw.cmd clean package -DskipTests
cd ..
```

### Вариант 3: Использование Git Bash

Если у вас установлен Git Bash, вы можете использовать Linux-версию скрипта:

```bash
./build.sh
```

## Запуск Docker Compose

После успешной сборки всех проектов:

```cmd
docker-compose up -d
```

Или через Docker Desktop:
- Откройте Docker Desktop
- Перейдите в папку проекта
- Используйте команду `docker-compose up -d` в терминале

## Проверка работы

Проверьте статус контейнеров:

```cmd
docker-compose ps
```

## Порты сервисов

- **8080** - Demo REST API
- **8081** - Analytics Service (HTTP/Actuator)
- **8082** - Audit Service
- **8083** - Notification Service
- **8085** - Jenkins
- **9090** - Analytics Service (gRPC)
- **9091** - Prometheus
- **9411** - Zipkin
- **3000** - Grafana
- **5433** - PostgreSQL
- **5672** - RabbitMQ
- **15672** - RabbitMQ Management UI

## Возможные проблемы

### Проблема: "mvnw.cmd не найден"

**Решение:** Убедитесь, что вы находитесь в правильной директории проекта. Maven Wrapper должен быть в каждой поддиректории проекта.

### Проблема: "Docker не запускается"

**Решение:** 
1. Убедитесь, что Docker Desktop запущен
2. Проверьте, что WSL 2 включен (если используете Windows 10/11)
3. Перезапустите Docker Desktop

### Проблема: "Порты заняты"

**Решение:** 
1. Проверьте, какие порты заняты: `netstat -ano | findstr :8080`
2. Остановите процессы, использующие порты, или измените порты в `docker-compose.yml`

### Проблема: "Ошибка при сборке"

**Решение:**
1. Убедитесь, что все контракты собраны перед сборкой сервисов
2. Проверьте, что у вас достаточно места на диске
3. Попробуйте очистить кеш Maven: удалите папку `%USERPROFILE%\.m2\repository`

## Альтернатива: Использование WSL 2

Если у вас Windows 10/11, вы можете использовать WSL 2 (Windows Subsystem for Linux):

1. Установите WSL 2: https://docs.microsoft.com/windows/wsl/install
2. Установите Ubuntu или другой Linux дистрибутив
3. В WSL выполните те же команды, что и для Linux:
   ```bash
   git clone https://github.com/krissstiina/flower.git
   cd flower
   ./build.sh
   docker-compose up -d
   ```

## Дополнительная информация

- Все команды Maven используют Maven Wrapper (`mvnw.cmd`), который не требует установки Maven
- Docker Compose работает одинаково на Windows, Linux и macOS
- Проект полностью кроссплатформенный

