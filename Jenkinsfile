pipeline {
    agent any

    // Кеширование Maven репозитория между сборками
    environment {
        MAVEN_OPTS = "-Dmaven.repo.local=${WORKSPACE}/.m2/repository"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build projects') {
            steps {
                script {
                    // Сначала собираем зависимости (contracts)
                    sh '''
                      cd flower-events-contract
                      ./mvnw install -DskipTests
                    '''
                    
                    sh '''
                      cd flower-api-contract
                      ./mvnw install -DskipTests
                    '''
                    
                    // Затем собираем сервисы параллельно (они независимы друг от друга)
                    parallel(
                        'demo-rest': {
                            sh '''
                              cd demo-rest-flower
                              ./mvnw package -DskipTests
                            '''
                        },
                        'analytics-service': {
                            sh '''
                              cd flower-analytics-service
                              ./mvnw package -DskipTests
                            '''
                        },
                        'audit-service': {
                            sh '''
                              cd flower-audit-service
                              ./mvnw package -DskipTests
                            '''
                        },
                        'notification-service': {
                            sh '''
                              cd notification-service
                              ./mvnw package -DskipTests
                            '''
                        }
                    )
                }
            }
        }

        stage('Docker Compose') {
            steps {
                sh '''
                  # Используем docker compose (новый синтаксис) или docker-compose
                  docker compose version > /dev/null 2>&1 && DOCKER_COMPOSE="docker compose" || DOCKER_COMPOSE="docker-compose"
                  
                  # Переходим в корневую директорию проекта
                  cd ${WORKSPACE}
                  
                  # Останавливаем старые контейнеры
                  ${DOCKER_COMPOSE} down || true
                  
                  # Собираем образы параллельно (если поддерживается)
                  ${DOCKER_COMPOSE} build --parallel || ${DOCKER_COMPOSE} build
                  
                  # Запускаем контейнеры
                  ${DOCKER_COMPOSE} up -d
                '''
            }
        }

        stage('Health check') {
            steps {
                script {
                    // Умный health check с retry вместо фиксированной задержки
                    def services = [
                        [name: 'demo-rest', url: 'http://localhost:8080/actuator/health'],
                        [name: 'analytics-service', url: 'http://localhost:8081/actuator/health'],
                        [name: 'audit-service', url: 'http://localhost:8082/actuator/health'],
                        [name: 'notification-service', url: 'http://localhost:8083/actuator/health']
                    ]
                    
                    services.each { service ->
                        def maxAttempts = 30
                        def waitTime = 2
                        def attempt = 0
                        def success = false
                        
                        while (attempt < maxAttempts && !success) {
                            attempt++
                            try {
                                def result = sh(
                                    script: "curl -f -s ${service.url} || exit 1",
                                    returnStatus: true
                                )
                                if (result == 0) {
                                    echo "✓ ${service.name} is healthy!"
                                    success = true
                                } else {
                                    if (attempt < maxAttempts) {
                                        sleep time: waitTime, unit: 'SECONDS'
                                    }
                                }
                            } catch (Exception e) {
                                if (attempt < maxAttempts) {
                                    sleep time: waitTime, unit: 'SECONDS'
                                }
                            }
                        }
                        
                        if (!success) {
                            error("${service.name} failed to become healthy after ${maxAttempts * waitTime} seconds")
                        }
                    }
                    
                    echo "All services are healthy!"
                }
            }
        }
    }

    post {
        success {
            echo 'OK: services are running'
        }
        failure {
            echo 'Build failed'
            sh 'docker-compose logs --tail=50 || docker compose logs --tail=50'
        }
    }
}
