#!/bin/bash

echo "=========================================="
echo "Сборка проектов Flower Shop"
echo "=========================================="

# Цвета для вывода
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Функция для проверки успешности сборки
check_build() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Сборка успешна${NC}"
    else
        echo -e "${YELLOW}✗ Ошибка при сборке${NC}"
        exit 1
    fi
}

# Шаг 1: Сборка контрактов
echo ""
echo -e "${YELLOW}Шаг 1: Сборка контрактов...${NC}"
echo ""

echo "Сборка flower-events-contract..."
cd flower-events-contract
chmod +x mvnw
./mvnw clean install -DskipTests
check_build
cd ..

echo "Сборка flower-api-contract..."
cd flower-api-contract
chmod +x mvnw
./mvnw clean install -DskipTests
check_build
cd ..

# Шаг 2: Сборка сервисов
echo ""
echo -e "${YELLOW}Шаг 2: Сборка сервисов...${NC}"
echo ""

echo "Сборка demo-rest-flower..."
cd demo-rest-flower
chmod +x mvnw
./mvnw clean package -DskipTests
check_build
cd ..

echo "Сборка flower-analytics-service..."
cd flower-analytics-service
chmod +x mvnw
./mvnw clean package -DskipTests
check_build
cd ..

echo "Сборка flower-audit-service..."
cd flower-audit-service
chmod +x mvnw
./mvnw clean package -DskipTests
check_build
cd ..

echo "Сборка notification-service..."
cd notification-service
chmod +x mvnw
./mvnw clean package -DskipTests
check_build
cd ..

echo ""
echo -e "${GREEN}=========================================="
echo "Все проекты успешно собраны!"
echo "Теперь можно запустить: docker-compose up -d"
echo "==========================================${NC}"

