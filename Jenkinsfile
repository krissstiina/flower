pipeline {
    agent any

    environment {
        // Кеширование Maven репозитория между сборками
        MAVEN_OPTS = "-Dmaven.repo.local=${WORKSPACE}/.m2/repository"
        // Оптимизация Maven для быстрой сборки (убрали -q чтобы видеть прогресс)
        MVN_OPTS = "-B -T 1C"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build contracts') {
            steps {
                script {
                    // Собираем контракты последовательно (они могут зависеть друг от друга)
                    echo "Building flower-events-contract..."
                    sh '''
                        cd flower-events-contract
                        ./mvnw install ${MVN_OPTS} -DskipTests
                    '''
                    echo "Building flower-api-contract..."
                    sh '''
                        cd flower-api-contract
                        ./mvnw install ${MVN_OPTS} -DskipTests
                    '''
                }
            }
        }

        stage('Build services') {
            steps {
                script {
                    // Собираем сервисы параллельно для ускорения
                    echo "Building all services in parallel..."
                    parallel(
                        'demo-rest': {
                            echo "Building demo-rest-flower..."
                            sh '''
                                cd demo-rest-flower
                                ./mvnw package ${MVN_OPTS} -DskipTests
                            '''
                        },
                        'analytics-service': {
                            echo "Building flower-analytics-service..."
                            sh '''
                                cd flower-analytics-service
                                ./mvnw package ${MVN_OPTS} -DskipTests
                            '''
                        },
                        'audit-service': {
                            echo "Building flower-audit-service..."
                            sh '''
                                cd flower-audit-service
                                ./mvnw package ${MVN_OPTS} -DskipTests
                            '''
                        },
                        'notification-service': {
                            echo "Building notification-service..."
                            sh '''
                                cd notification-service
                                ./mvnw package ${MVN_OPTS} -DskipTests
                            '''
                        }
                    )
                    echo "All services built successfully!"
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    # Определяем команду docker compose
                    DOCKER_COMPOSE="docker compose"
                    docker compose version > /dev/null 2>&1 || DOCKER_COMPOSE="docker-compose"
                    
                    cd ${WORKSPACE}
                    
                    # Останавливаем и удаляем старые контейнеры (кроме Jenkins)
                    ${DOCKER_COMPOSE} stop postgres rabbitmq zipkin prometheus grafana demo-rest analytics-service audit-service notification-service 2>/dev/null || true
                    ${DOCKER_COMPOSE} rm -f postgres rabbitmq zipkin prometheus grafana demo-rest analytics-service audit-service notification-service 2>/dev/null || true
                    
                    # Также удаляем контейнеры напрямую через docker, если они все еще существуют
                    docker rm -f flower_shop_db flower_rabbitmq flower_zipkin flower_prometheus flower_grafana demo-rest analytics-service audit-service notification-service 2>/dev/null || true
                    
                    # Собираем образы с кешированием слоев (без Jenkins)
                    ${DOCKER_COMPOSE} build --parallel demo-rest analytics-service audit-service notification-service || ${DOCKER_COMPOSE} build demo-rest analytics-service audit-service notification-service
                    
                    # Запускаем контейнеры (без Jenkins)
                    ${DOCKER_COMPOSE} up -d postgres rabbitmq zipkin prometheus grafana demo-rest analytics-service audit-service notification-service
                '''
            }
        }

        stage('Health Check') {
            steps {
                script {
                    def services = [
                        [name: 'demo-rest', url: 'http://localhost:8080/actuator/health'],
                        [name: 'analytics-service', url: 'http://localhost:8081/actuator/health'],
                        [name: 'audit-service', url: 'http://localhost:8082/actuator/health'],
                        [name: 'notification-service', url: 'http://localhost:8083/actuator/health']
                    ]
                    
                    // Упрощенный health check с таймаутом
                    services.each { service ->
                        def maxAttempts = 20
                        def waitTime = 3
                        def success = false
                        
                        for (int i = 0; i < maxAttempts; i++) {
                            def result = sh(
                                script: "curl -f -s --max-time 2 ${service.url} || exit 1",
                                returnStatus: true
                            )
                            if (result == 0) {
                                echo "✓ ${service.name} is healthy"
                                success = true
                                break
                            }
                            if (i < maxAttempts - 1) {
                                sleep time: waitTime, unit: 'SECONDS'
                            }
                        }
                        
                        if (!success) {
                            error("${service.name} failed to become healthy")
                        }
                    }
                    
                    echo "All services are healthy!"
                }
            }
        }
    }

    post {
        success {
            echo '✓ Build successful - all services are running'
        }
        failure {
            echo '✗ Build failed'
            sh '''
                DOCKER_COMPOSE="docker compose"
                docker compose version > /dev/null 2>&1 || DOCKER_COMPOSE="docker-compose"
                ${DOCKER_COMPOSE} logs --tail=100 || true
            '''
        }
    }
}
