pipeline {
    agent any

    // Автоматическая сборка при изменениях в Git
    triggers {
        // Проверяет изменения каждую минуту
        pollSCM('H/1 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    try {
                        env.JAVA_HOME = tool 'jdk-21'
                        env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
                    } catch (Exception e) {
                        echo "JDK не настроен в Jenkins, используем системный Java"
                    }
                }
                sh 'which java || true'
                sh 'java -version'
                sh 'javac -version || true'
                sh 'echo "JAVA_HOME=${JAVA_HOME:-not set}"'
                checkout scm
                sh 'git submodule init || true'
                sh 'git submodule update --recursive --remote || true'
            }
        }

        stage('Build') {
            steps {
                script {
                    sh 'cd flower-events-contract && chmod +x mvnw && ./mvnw -B clean install -DskipTests'
                    sh 'cd flower-api-contract && chmod +x mvnw && ./mvnw -B clean install -DskipTests'
                    sh 'cd demo-rest-flower && chmod +x mvnw && ./mvnw -B clean package -DskipTests'
                    sh 'cd flower-analytics-service && chmod +x mvnw && ./mvnw -B clean package -DskipTests'
                    sh 'cd flower-audit-service && chmod +x mvnw && ./mvnw -B clean package -DskipTests'
                    sh 'cd notification-service && chmod +x mvnw && ./mvnw -B clean package -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    sh 'docker --version'
                    sh 'docker compose --version || docker compose version'

                    sh 'docker compose build demo-rest analytics-service audit-service notification-service'
                }
            }
        }
    }
}
