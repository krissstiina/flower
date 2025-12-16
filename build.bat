@echo off
REM ==========================================
REM Сборка проектов Flower Shop (Windows)
REM ==========================================

setlocal enabledelayedexpansion

echo.
echo ==========================================
echo Сборка проектов Flower Shop
echo ==========================================
echo.

REM Шаг 1: Сборка контрактов
echo.
echo Шаг 1: Сборка контрактов...
echo.

echo Сборка flower-events-contract...
cd flower-events-contract
if exist mvnw.cmd (
    call mvnw.cmd clean install -DskipTests
) else (
    echo Ошибка: mvnw.cmd не найден!
    exit /b 1
)
if errorlevel 1 (
    echo Ошибка при сборке flower-events-contract
    exit /b 1
)
cd ..

echo Сборка flower-api-contract...
cd flower-api-contract
if exist mvnw.cmd (
    call mvnw.cmd clean install -DskipTests
) else (
    echo Ошибка: mvnw.cmd не найден!
    exit /b 1
)
if errorlevel 1 (
    echo Ошибка при сборке flower-api-contract
    exit /b 1
)
cd ..

REM Шаг 2: Сборка сервисов
echo.
echo Шаг 2: Сборка сервисов...
echo.

echo Сборка demo-rest-flower...
cd demo-rest-flower
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo Ошибка: mvnw.cmd не найден!
    exit /b 1
)
if errorlevel 1 (
    echo Ошибка при сборке demo-rest-flower
    exit /b 1
)
cd ..

echo Сборка flower-analytics-service...
cd flower-analytics-service
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo Ошибка: mvnw.cmd не найден!
    exit /b 1
)
if errorlevel 1 (
    echo Ошибка при сборке flower-analytics-service
    exit /b 1
)
cd ..

echo Сборка flower-audit-service...
cd flower-audit-service
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo Ошибка: mvnw.cmd не найден!
    exit /b 1
)
if errorlevel 1 (
    echo Ошибка при сборке flower-audit-service
    exit /b 1
)
cd ..

echo Сборка notification-service...
cd notification-service
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo Ошибка: mvnw.cmd не найден!
    exit /b 1
)
if errorlevel 1 (
    echo Ошибка при сборке notification-service
    exit /b 1
)
cd ..

echo.
echo ==========================================
echo Все проекты успешно собраны!
echo Теперь можно запустить: docker-compose up -d
echo ==========================================
echo.

endlocal

