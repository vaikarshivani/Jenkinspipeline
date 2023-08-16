pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Run Docker') {
            steps {
                script {
                    def dockerImage = docker.build("my-java-app:${env.BUILD_ID}", "-f Dockerfile .")

                    dockerImage.inside("--rm -v $PWD:/app -w /app") {
                        sh "mvn clean package"
                    }
                }
            }
        }
    }
}
