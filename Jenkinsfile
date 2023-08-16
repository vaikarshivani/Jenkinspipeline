pipeline {
    agent any

    environment {
        mavenTool = 'Maven 3.9.4'
        gitRemoteUrl = 'https://github.com/shivanititan/Firebase.git'
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
                script {
                   def artifactDir = "D:\\artifacts" // Change this path to the desired location on the D drive
                   bat "mkdir ${artifactDir}"
                   bat "copy token\\target\\*.jar ${artifactDir}\\"
                }
            }
        }

        stage('Commit and push artifact') {
            steps {
                script {
                        def gitActor = env.GITHUB_ACTOR
                        def gitEmail = "${gitActor}@users.noreply.github.com"
            
                        bat "git config --global user.name ${gitActor}"
                        bat "git config --global user.email ${gitEmail}"
            
                        bat 'git add artifacts/'
                        bat 'git commit -m "Add built artifact"'
                        bat 'git push origin HEAD:refs/heads/main'
                    
                //     checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'GITHUB_TOKEN', url:gitRemoteUrl]]])
                    
                //     try {
                //         bat 'git add artifacts/'
                //         bat 'git commit -m "Add built artifact"'
                //         bat 'git push origin HEAD:refs/heads/main'
                //     } catch (Exception e) {
                //         echo "Error pushing artifact to GitHub: ${e.getMessage()}"
                //         echo "git error: ${error}"
                //     }
                // }
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
