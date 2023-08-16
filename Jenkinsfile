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
               
                bat 'copy token\\target\\*.jar artifacts\\'
            }
        }

        stage('Commit and push artifact') {
            steps {
                withCredentials([gitUsernamePassword(credentialsId: 'f9cb8e69-ab76-4897-a27f-bfa66dcbd1b8', gitToolName: 'Default')]) {
                    script {
                        try {
                            bat 'git add artifacts/'
                            bat 'git commit -m "Add built artifact"'
                            bat 'git push origin HEAD:refs/heads/main'
                        } catch (Exception e) {
                            error "Error pushing artifact to GitHub: ${e.getMessage()}"
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
