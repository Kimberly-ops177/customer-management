pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKERHUB_USERNAME = 'kimberlyops'
        IMAGE_NAME = 'customer-management'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                echo '📥 Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '🔨 Building with Gradle...'
                sh './gradlew clean build -x test --no-daemon'
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Running JUnit tests...'
                sh './gradlew test --no-daemon'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                    echo '📊 Test results published'
                }
            }
        }

        stage('Code Coverage') {
            steps {
                echo '📈 Generating JaCoCo coverage report...'
                sh './gradlew jacocoTestReport --no-daemon'
            }
            post {
                always {
                    publishHTML(target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'build/reports/jacoco/test/html',
                        reportFiles: 'index.html',
                        reportName: 'JaCoCo Coverage Report'
                    ])
                }
            }
        }

        stage('Coverage Verification') {
            steps {
                echo '✅ Verifying 80% test coverage...'
                sh './gradlew jacocoTestCoverageVerification --no-daemon'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '🐳 Building Docker image...'
                sh """
                    docker build -t ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG} .
                    docker tag ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG} ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:latest
                """
            }
        }

        stage('Push to DockerHub') {
            steps {
                echo '🚀 Pushing image to DockerHub...'
                sh """
                    echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin
                    docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG}
                    docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:latest
                """
            }
        }

        stage('Cleanup') {
            steps {
                echo '🧹 Cleaning up local Docker images...'
                sh """
                    docker rmi ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG} || true
                    docker rmi ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:latest || true
                """
            }
        }
    }

    post {
        success {
            echo """
            ✅ Pipeline SUCCESS!
            Image pushed: ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG}
            DockerHub: https://hub.docker.com/r/${DOCKERHUB_USERNAME}/${IMAGE_NAME}
            """
        }
        failure {
            echo '❌ Pipeline FAILED! Check the logs above.'
        }
        always {
            echo '🏁 Pipeline finished.'
        }
    }
}
