pipeline {
    agent any

    environment {
        mavenTool = 'Maven 3.9.4'
    }
    
    stages {
        stage('Checkout code') {
            steps {
                checkout scm
            }
        }

        stage('Set up JDK') {
            steps {
                tool name: 'JDK11', type: 'jdk'
            }
        }

        stage('Build with Maven') {
            steps {
                tool name: mavenTool, type: 'hudson.tasks.Maven$MavenInstallation'
                bat "\"${tool(name: mavenTool, type: 'hudson.tasks.Maven$MavenInstallation')}/bin/mvn\" -B clean compile package --file token/pom.xml"
            }
        }

        stage('Execute Java Program') {
            steps {
                bat 'java -cp token/target/restdemo-0.0.1-SNAPSHOT.jar com.google.firebase.samples.config.TemplateConfigure'
            }
        }
    }
}
