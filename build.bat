@echo off

setlocal enabledelayedexpansion

echo.
echo ==========================================
echo Building Flower Shop projects
echo ==========================================
echo.

REM Step 1: Building contracts
echo.
echo Step 1: Building contracts...
echo.

echo Building flower-events-contract...
cd flower-events-contract
if exist mvnw.cmd (
    call mvnw.cmd clean install -DskipTests
) else (
    echo Error: mvnw.cmd not found!
    exit /b 1
)
if errorlevel 1 (
    echo Error building flower-events-contract
    exit /b 1
)
cd ..

echo Building flower-api-contract...
cd flower-api-contract
if exist mvnw.cmd (
    call mvnw.cmd clean install -DskipTests
) else (
    echo Error: mvnw.cmd not found!
    exit /b 1
)
if errorlevel 1 (
    echo Error building flower-api-contract
    exit /b 1
)
cd ..

REM Step 2: Building services
echo.
echo Step 2: Building services...
echo.

echo Building demo-rest-flower...
cd demo-rest-flower
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo Error: mvnw.cmd not found!
    exit /b 1
)
if errorlevel 1 (
    echo Error building demo-rest-flower
    exit /b 1
)
cd ..

echo Building flower-analytics-service...
cd flower-analytics-service
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo Error: mvnw.cmd not found!
    exit /b 1
)
if errorlevel 1 (
    echo Error building flower-analytics-service
    exit /b 1
)
cd ..

echo Building flower-audit-service...
cd flower-audit-service
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo Error: mvnw.cmd not found!
    exit /b 1
)
if errorlevel 1 (
    echo Error building flower-audit-service
    exit /b 1
)
cd ..

echo Building notification-service...
cd notification-service
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo Error: mvnw.cmd not found!
    exit /b 1
)
if errorlevel 1 (
    echo Error building notification-service
    exit /b 1
)
cd ..

echo.
echo ==========================================
echo All projects built successfully!
echo Now you can run: docker-compose up -d
echo ==========================================
echo.

endlocal

