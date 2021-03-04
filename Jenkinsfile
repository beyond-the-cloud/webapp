pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building the application...'
                build job: 'BuildWebapp'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying the application...'
                build job: 'DeployWebapp'
            }
        }
    }
}
