pipeline {
    agent any

    environment {
        JDK_HOME = tool name: 'JDK11', type: 'jdk' // Update the tool name to match your JDK configuration
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout your source code from version control (e.g., Git)
                checkout scm
            }
        }

        stage('Build and Run') {
            steps {
                script {
                    def javaCmd = "\"${env.JDK_HOME}\bin\java\""
                    def javacCmd = "\"${env.JDK_HOME}\bin\javac\""

                    // Compile the Java class
                    bat "${javacCmd} -classpath . TemplateConfigure.java"

                    // Run the Java class
                    bat "start ${javaCmd} -classpath . TemplateConfigure"
                }
            }
        }
    }
}
