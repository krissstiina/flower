pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build projects') {
            steps {
                sh '''
                  cd flower-events-contract
                  ./mvnw clean install -DskipTests

                  cd ../demo-rest-flower
                  ./mvnw clean package -DskipTests

                  cd ../flower-analytics-service
                  ./mvnw clean package -DskipTests

                  cd ../flower-audit-service
                  ./mvnw clean package -DskipTests

                  cd ../notification-service
                  ./mvnw clean package -DskipTests
                '''
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
                  
                  # Запускаем новые контейнеры
                  ${DOCKER_COMPOSE} up --build -d
                '''
            }
        }

        stage('Health check') {
            steps {
                sh '''
                  sleep 30
                  echo "Checking demo-rest health..."
                  curl -f http://localhost:8080/actuator/health || exit 1
                  echo "Checking analytics-service health..."
                  curl -f http://localhost:8081/actuator/health || exit 1
                  echo "Checking audit-service health..."
                  curl -f http://localhost:8082/actuator/health || exit 1
                  echo "Checking notification-service health..."
                  curl -f http://localhost:8083/actuator/health || exit 1
                  echo "All services are healthy!"
                '''
            }
        }
    }

    post {
        success {
            echo 'OK: services are running'
        }
        failure {
            echo 'Build failed'
            sh 'docker-compose logs --tail=50'
        }
    }
}
