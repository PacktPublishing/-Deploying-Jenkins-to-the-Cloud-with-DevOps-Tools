import jenkins.*
import hudson.*
import hudson.model.*
import jenkins.model.*
import hudson.security.*
import hudson.slaves.*
import hudson.tools.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.jenkins.plugins.sshcredentials.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import hudson.plugins.sshslaves.*
import java.util.ArrayList
import hudson.slaves.EnvironmentVariablesNodeProperty.Entry

def create_node = { instance, launcher, hostname, name, label, javaHome, home="/home/jenkins" ->
    // Define a "Permanent Agent"
    DumbSlave agent = new DumbSlave(
            name,
            home,
            launcher)
    agent.nodeDescription = name
    agent.numExecutors = 1
    agent.labelString = label
    agent.mode = Node.Mode.NORMAL
    agent.retentionStrategy = new RetentionStrategy.Always()

    List<Entry> env = new ArrayList<Entry>();
    env.add(new Entry('JAVA_HOME', javaHome))
    EnvironmentVariablesNodeProperty envPro = new EnvironmentVariablesNodeProperty(env);

    agent.getNodeProperties().add(envPro)

  	JDK.DescriptorImpl jdkDescriptor = instance.getDescriptorByType(JDK.DescriptorImpl.class);
    def property = new ToolLocationNodeProperty(
      new ToolLocationNodeProperty.ToolLocation(jdkDescriptor, "Java", javaHome)
    )

    agent.getNodeProperties().add(property)

    // Create a "Permanent Agent"
    instance.addNode(agent)
}

def create_ssh_node  = { instance, hostname, sshPrivateKey, name, label, javaHome, home="/home/jenkins" ->
  // Retrieve the Global credential store
  def domain = com.cloudbees.plugins.credentials.domains.Domain.global()
  def store = instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

  // Create the SSH credential
  def jenkins_creds = new com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey(
    com.cloudbees.plugins.credentials.CredentialsScope.GLOBAL,
    "ssh_node_${name}",
    'jenkins',
    new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource(sshPrivateKey),
    (String)null,
    (String)null
  )
  store.addCredentials(domain, jenkins_creds)

    // Define a "Launch method": "Launch slave agents via SSH"
    ComputerLauncher launcher = new SSHLauncher(
            hostname, // Host
            22, // Port
            "ssh_node_${name}", // Credentials
            (String)null, // JVM Options
            (String)null, // JavaPath
            (String)null, // Prefix Start Slave Command
            (String)null, // Suffix Start Slave Command
            (Integer)null, // Connection Timeout in Seconds
            (Integer)null, // Maximum Number of Retries
            (Integer)null // The number of seconds to wait between retries
    )
    create_node(instance, launcher, hostname, name, label, javaHome, home)
}