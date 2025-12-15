pipeline {
    agent any

    tools {
        maven 'maven-3.9.9'
    }

    environment {
        JAVA_HOME = tool 'jdk-21'
        PATH = "${JAVA_HOME}/bin:${PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                sh 'which java || true'
                sh 'java -version'
                sh 'javac -version'
                sh 'echo "JAVA_HOME=$JAVA_HOME"'
                sh 'mvn -v'
                checkout scm
                sh 'git submodule init || true'
                sh 'git submodule update --recursive --remote || true'
            }
        }

        stage('Build') {
            steps {
                script {
                    // Собираем контракты сначала
                    sh '''
                        cd flower-events-contract
                        mvn -B clean install -DskipTests
                    '''
                    sh '''
                        cd flower-api-contract
                        mvn -B clean install -DskipTests
                    '''
                    // Затем собираем сервисы
                    sh '''
                        cd demo-rest-flower
                        mvn -B clean package -DskipTests
                    '''
                    sh '''
                        cd flower-analytics-service
                        mvn -B clean package -DskipTests
                    '''
                    sh '''
                        cd flower-audit-service
                        mvn -B clean package -DskipTests
                    '''
                    sh '''
                        cd notification-service
                        mvn -B clean package -DskipTests
                    '''
                }
            }
        }

        stage('Docker Compose Build') {
            steps {
                script {
                    sh 'docker --version'
                    sh 'docker compose --version || docker compose version'

                    sh '''
                        DOCKER_COMPOSE="docker compose"
                        if ! docker compose version > /dev/null 2>&1; then
                            DOCKER_COMPOSE="docker-compose"
                        fi
                        
                        cd ${WORKSPACE}
                        
                        ${DOCKER_COMPOSE} down --remove-orphans || true
                        
                        docker rm -f flower_shop_db flower_rabbitmq flower_zipkin flower_prometheus flower_grafana demo-rest analytics-service audit-service notification-service 2>/dev/null || true
                        
                        ${DOCKER_COMPOSE} build --parallel demo-rest analytics-service audit-service notification-service || ${DOCKER_COMPOSE} build demo-rest analytics-service audit-service notification-service
                        
                        ${DOCKER_COMPOSE} up -d postgres rabbitmq zipkin prometheus grafana demo-rest analytics-service audit-service notification-service
                    '''
                }
            }
        }
    }
}
