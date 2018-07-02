properties properties: [
        [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '5', numToKeepStr: '10']],
        [$class: 'GitLabConnectionProperty', gitLabConnection: 'gitlab'],
        disableConcurrentBuilds()
]

@Library('my-build-library')
def maven = new de.mare.ci.jenkins.Maven()

node {
    try {

      stage('Checkout') {
          checkout scm 
      }

      stage('Project Info') {
          echo "Starting building of version " + maven.getProjectVersion()
          archiveArtifacts artifacts: 'target/*.jar'
      }

      stage('Build') {
          sh "./mvnw clean package -U -DskipTests"
          archiveArtifacts artifacts: 'target/*.jar'
      }

      stage('Unit-Tests') {
        try {
            sh "./mvnw test"
        } finally {
            junit healthScaleFactor: 1.0, testResults: 'target/surefire-reports/TEST*.xml,target/failsafe-reports/TEST*.xml'
        }
      }

      stage('End2End-Tests') {
        // TODO
      }

      stage('Deploy') {
        // TODO
      }


    } catch (e) {
      step([$class                  : 'Mailer',
            notifyEveryUnstableBuild: true,
            recipients              : "build-notification@example.com",
            sendToIndividuals       : true])
      throw e
    }
}
