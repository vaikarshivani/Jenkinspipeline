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

        stage('Store artifact') {
            steps {
                script{
                // bat 'mkdir artifacts'
                bat 'copy token\\target\\*.jar artifacts\\'
            }
        }

        stage('Commit and push artifact') {
            steps {
                withCredentials([gitUsernamePassword(credentialsId: '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', variable: 'GIT_TOKEN')]) {
                    script {
                        try {
                            bat 'git add artifacts/'
                            bat 'git commit -m "Add built artifact"'
                            bat 'git push origin HEAD:refs/heads/main'
                        } catch (Exception e) {
                            echo "Error pushing artifact to GitHub: ${e.getMessage()}"
                        }
                    }
                }
            }
        }

        stage('Compile and Run Java Program') {
            steps {
                script {
                    def javaCmd = "${tool(name: 'JDK11', type: 'jdk')}/bin/java"
                    bat "\"${javaCmd}\" -cp token/target/Firebase-0.0.1-SNAPSHOT.jar com.google.firebase.samples.config.TemplateConfigure"
                }
            }
        }
    }
}
