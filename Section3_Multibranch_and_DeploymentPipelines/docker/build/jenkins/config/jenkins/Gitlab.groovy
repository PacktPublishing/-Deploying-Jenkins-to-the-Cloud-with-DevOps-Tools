import jenkins.model.*
import hudson.util.Secret
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.dabsquared.gitlabjenkins.connection.*

// Configure the global git tool
def gitlab = { instance, name, url, credentialsId, token ->

  // add credentials
  global_domain = Domain.global()
  credentials_store =  instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
  credentials = new GitLabApiTokenImpl(CredentialsScope.GLOBAL, credentialsId, credentialsId, Secret.fromString(token))
  credentials_store.addCredentials(global_domain, credentials)

  // create connection
  GitLabConnectionConfig descriptor = (GitLabConnectionConfig) instance.getDescriptor(GitLabConnectionConfig.class)

  GitLabConnection gitLabConnection = new GitLabConnection(name, url, credentialsId, true, 10, 10)
  descriptor.getConnections().clear()
  descriptor.addConnection(gitLabConnection)
  descriptor.save()
}
