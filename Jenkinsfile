pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Jenkins Pipeline ayarlarında SCM kullandığımız için burası otomatik yapılır,
                // ancak manuel kalacaksa şu yeterlidir:
                checkout scm
            }
        }

        stage('Build & Deploy') {
            steps {
                // sudo kaldırıldı ve 'docker compose' (tire olmadan) deniyoruz
                sh '/usr/bin/docker-compose up --build -d gelir-gider-app'
            }
        }

        stage('Cleanup') {
            steps {
                sh 'docker image prune -f'
            }
        }
    }
}