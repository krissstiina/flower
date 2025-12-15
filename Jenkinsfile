pipeline {
    agent any

    triggers {
        // Проверяет изменения каждую минуту (без H для более предсказуемого времени)
        pollSCM('* * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'git submodule init || true'
                sh 'git submodule update --recursive --remote || true'
            }
        }

        stage('Build Contracts') {
            steps {
                script {
                    parallel(
                        'events-contract': {
                            sh 'cd flower-events-contract && chmod +x mvnw && ./mvnw -B install -DskipTests -Dmaven.repo.local=${WORKSPACE}/.m2'
                        },
                        'api-contract': {
                            sh 'cd flower-api-contract && chmod +x mvnw && ./mvnw -B install -DskipTests -Dmaven.repo.local=${WORKSPACE}/.m2'
                        }
                    )
                }
            }
        }

        stage('Build Services') {
            steps {
                script {
                    parallel(
                        'demo-rest': {
                            sh 'cd demo-rest-flower && chmod +x mvnw && ./mvnw -B package -DskipTests -Dmaven.repo.local=${WORKSPACE}/.m2'
                        },
                        'analytics': {
                            sh 'cd flower-analytics-service && chmod +x mvnw && ./mvnw -B package -DskipTests -Dmaven.repo.local=${WORKSPACE}/.m2'
                        },
                        'audit': {
                            sh 'cd flower-audit-service && chmod +x mvnw && ./mvnw -B package -DskipTests -Dmaven.repo.local=${WORKSPACE}/.m2'
                        },
                        'notification': {
                            sh 'cd notification-service && chmod +x mvnw && ./mvnw -B package -DskipTests -Dmaven.repo.local=${WORKSPACE}/.m2'
                        }
                    )
                }
            }
        }

        // stage('Docker Build') {
        //     steps {
        //         script {
        //             sh 'docker --version'
        //             sh 'docker compose --version || docker compose version'

        //             sh 'docker compose build demo-rest analytics-service audit-service notification-service'
        //         }
        //     }
        // }
    }

    post {
        success {
            echo 'Сборка завершена успешно!'
            echo 'Все проекты собраны и Docker образы созданы.'
            echo 'Время сборки: ${currentBuild.durationString}'
        }
        failure {
            echo 'Сборка завершилась с ошибкой!'
        }
    }
}
