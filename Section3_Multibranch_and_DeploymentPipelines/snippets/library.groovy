@Library('my-build-library')
def maven = new de.mare.ci.jenkins.Maven()

node {
    stage('Checkout') {
        checkout scm
    }

    stage('Build') {
        echo "Starting building of version " + maven.getProjectVersion()
        sh './mvnw clean package'
    }
}
