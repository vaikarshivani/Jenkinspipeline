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

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'token/target/Firebase-0.0.1-SNAPSHOT.jar', allowEmptyArchive: true
            }
        }

        stage('Compile and Run Java Program') {
            steps {
                def javaCmd = "${tool(name: 'JDK11', type: 'jdk')}/bin/java"
                bat "\"${javaCmd}\" -cp token/target/Firebase-0.0.1-SNAPSHOT.jar com.firebase.template.TemplateConfigure"
            }
        }
    }
}
