import jenkins.*
import hudson.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.jenkins.plugins.sshcredentials.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import hudson.plugins.sshslaves.*
import hudson.model.*
import jenkins.model.*
import hudson.security.*


// Configure global ssh credentials
def ssh_credential = { instance, id, username, description, keyfile, passphrase='' ->
  // Retrieve the Global credential store
  def domain = com.cloudbees.plugins.credentials.domains.Domain.global()
  def store = instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

  // Create the SSH credential
  def jenkins_creds = new com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey(
    com.cloudbees.plugins.credentials.CredentialsScope.GLOBAL,
    id,
    username,
    new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource(keyfile),
    passphrase,
    description
  )
  store.addCredentials(domain, jenkins_creds)
  println "Adding ${id} ssh credentials"
}
