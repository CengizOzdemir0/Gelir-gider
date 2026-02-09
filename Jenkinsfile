pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/CengizOzdemir0/Gelir-gider.git'
            }
        }

        stage('Build & Deploy') {
            steps {
                sh 'sudo docker-compose up -d --build gelir-gider-app'
            }
        }

        stage('Cleanup') {
            steps {
                sh 'sudo docker image prune -f'
            }
        }
    }
}