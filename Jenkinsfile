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

        stage('Run Spring Boot App') {
            steps {
                bat 'mvn spring-boot:run'
            }
        }

    }
}