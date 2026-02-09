pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Deploy') {
            steps {
                script {
                    // Docker image'ı build et
                    sh 'docker build -t gelir-gider-app:latest .'
                    
                    // Eski container'ı durdur ve sil
                    sh 'docker stop gelir_gider_backend || true'
                    sh 'docker rm gelir_gider_backend || true'
                    
                    // Yeni container'ı başlat
                    sh '''
                        docker run -d \
                          --name gelir_gider_backend \
                          --network gelir-gider_gelir_gider_network \
                          -p 1818:1818 \
                          -p 4000:4000 \
                          -v glowroot_data:/app/glowroot/data \
                          -e SPRING_DATASOURCE_URL=jdbc:postgresql://gelir_gider_postgres:5432/gelir_gider_db \
                          -e SPRING_DATASOURCE_USERNAME=gelir_user \
                          -e SPRING_DATASOURCE_PASSWORD=gelir_sifre_123 \
                          -e SPRING_DATA_REDIS_HOST=gelir_gider_redis \
                          -e SPRING_DATA_REDIS_PORT=6379 \
                          gelir-gider-app:latest
                    '''
                }
            }
        }

        stage('Cleanup') {
            steps {
                sh 'docker image prune -f'
            }
        }
    }
}