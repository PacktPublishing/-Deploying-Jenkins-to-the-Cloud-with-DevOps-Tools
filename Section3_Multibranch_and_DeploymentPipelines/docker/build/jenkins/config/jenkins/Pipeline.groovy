import java.util.Arrays
import java.util.UUID
import org.jenkinsci.plugins.workflow.libs.*
import jenkins.plugins.git.GitSCMSource
import com.dabsquared.gitlabjenkins.connection.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl

// Configure the global pipeline library
def pipelineLibrary = { instance, name, gitUrl, credentialsId, user, password, defaultVersion = '1.0.0' ->

  // add credentials
  global_domain = Domain.global()
  credentials_store =  instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
  credentials = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, credentialsId, credentialsId, user, password)
  credentials_store.addCredentials(global_domain, credentials)

  // add scm link
  GlobalLibraries descriptor = (GlobalLibraries) GlobalLibraries.get()

  GitSCMSource scmSource = new GitSCMSource(UUID.randomUUID().toString(),
                                      gitUrl,
                                      credentialsId,
                                      'origin',
                                      '+refs/heads/*:refs/remotes/origin/*',
                                      '*',
                                      '',
                                      false)

  SCMSourceRetriever retriever = new SCMSourceRetriever(scmSource)

  LibraryConfiguration libraryConfiguration = new LibraryConfiguration(name, retriever)
  libraryConfiguration.setDefaultVersion(defaultVersion)
  libraryConfiguration.setImplicit(false)
  libraryConfiguration.setAllowVersionOverride(true)

  descriptor.setLibraries(Arrays.asList(libraryConfiguration))
  descriptor.save()
}
