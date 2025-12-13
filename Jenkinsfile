pipeline {
    agent any
    
    environment {
        DOCKER_COMPOSE = 'docker-compose'
        PROJECT_NAME = 'flower-shop'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from Git...'
                checkout scm
            }
        }
        
        stage('Build Events Contract') {
            steps {
                echo 'Building events-contract...'
                dir('flower-events-contract') {
                    sh './mvnw clean install -DskipTests'
                }
            }
        }
        
        stage('Build Demo REST') {
            steps {
                echo 'Building demo-rest-flower...'
                dir('demo-rest-flower') {
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
        
        stage('Build Analytics Service') {
            steps {
                echo 'Building analytics-service...'
                dir('flower-analytics-service') {
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
        
        stage('Build Audit Service') {
            steps {
                echo 'Building audit-service...'
                dir('flower-audit-service') {
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
        
        stage('Build Notification Service') {
            steps {
                echo 'Building notification-service...'
                dir('notification-service') {
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                echo 'Building Docker images...'
                sh '${DOCKER_COMPOSE} build --no-cache'
            }
        }
        
        stage('Stop Old Containers') {
            steps {
                echo 'Stopping old containers...'
                sh '${DOCKER_COMPOSE} down || true'
            }
        }
        
        stage('Start Services') {
            steps {
                echo 'Starting all services...'
                sh '${DOCKER_COMPOSE} up -d'
            }
        }
        
        stage('Health Check') {
            steps {
                echo 'Waiting for services to be healthy...'
                sleep(time: 30, unit: 'SECONDS')
                script {
                    def services = [
                        'demo-rest:8080/actuator/health',
                        'audit-service:8082/actuator/health',
                        'analytics-service:8081/actuator/health',
                        'notification-service:8083/actuator/health'
                    ]
                    
                    services.each { service ->
                        sh """
                            for i in {1..30}; do
                                if curl -f http://localhost:${service} > /dev/null 2>&1; then
                                    echo "Service ${service} is healthy"
                                    break
                                fi
                                echo "Waiting for ${service}... (attempt \$i/30)"
                                sleep 2
                            done
                        """
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline succeeded!'
            echo 'Services are running:'
            echo '- Demo REST: http://localhost:8080'
            echo '- Analytics: http://localhost:8081'
            echo '- Audit: http://localhost:8082'
            echo '- Notification: http://localhost:8083'
            echo '- Prometheus: http://localhost:9090'
            echo '- Grafana: http://localhost:3000'
            echo '- Zipkin: http://localhost:9411'
        }
        failure {
            echo 'Pipeline failed!'
            sh '${DOCKER_COMPOSE} logs --tail=100'
        }
        always {
            echo 'Cleaning up...'
        }
    }
}

