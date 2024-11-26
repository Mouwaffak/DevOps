pipeline {
    agent any

    environment {
        SONARQUBE_ENV = 'SonarQube'                // SonarQube environment name
        NEXUS_CREDENTIALS_ID = 'deploymentRepo'     // Nexus credentials ID in Jenkins
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials')
        RELEASE_VERSION = "1.0"
        registry = "" // Your Git repository name
        registryCredential = '' // DockerHub Credentials
        IMAGE_TAG = "${RELEASE_VERSION}-${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling code from Git...'
                git(
                    branch: '', // Branch name
                    credentialsId: 'github_pat',
                    url: '' // Repository Url
                )
            }
        }

        stage('Clean') {
            steps {
                echo 'Cleaning the workspace...'
                sh 'mvn clean'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging the application...'
                sh 'mvn package -Dmaven.test.skip=true'
            }
        }

        stage('Build') {
            steps {
                echo 'Building the application...'
                sh 'mvn install -Dmaven.test.skip=true'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    echo 'Running SonarQube analysis...'
                    withSonarQubeEnv(SONARQUBE_ENV) {
                        sh 'mvn sonar:sonar -Dsonar.projectKey=Devops2'
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                sh "mvn deploy -Dmaven.test.skip=true -DaltDeploymentRepository=deploymentRepo::default::http://192.168.56.10:8081/repository/maven-releases/"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    sh "docker build -t ${registry}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Login to Docker') {
            steps {
                script {
                    echo 'Logging into Docker Hub...'
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                    }
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    echo 'Pushing Docker image to Docker Hub...'
                    sh "docker push ${registry}:${IMAGE_TAG}"
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    echo 'Deploying with Docker Compose...'

                    // Update docker-compose.yml to use the new image tag
                    sh "sed -i 's|image: ${registry}:.*|image: ${registry}:${IMAGE_TAG}|' docker-compose.yml"

                    // Stop existing containers if running
                    sh 'docker compose down || true'

                    // Start the services
                    sh 'docker compose up -d'

                    // Wait for services to initialize
                    sh 'sleep 30'

                    // Verify deployment status
                    sh 'docker compose ps'
                }
            }
        }
    }

    post {
        success {
            echo 'Build finished successfully!'
        }
        failure {
            echo 'Build failed!'
        }
        always {
            cleanWs() // Clean workspace after build, regardless of success or failure
        }
    }
}
