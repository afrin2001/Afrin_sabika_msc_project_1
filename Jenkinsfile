pipeline {
    agent any


    stages {

        stage('Clone Repository') {
            steps {
                checkout scm
            }
        }

        stage('Build Project') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Deploy') {
            steps {
                bat 'java -jar target/student-cicd-1.0.jar'
            }
        }

    }
}