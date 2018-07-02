// Create user credentials
def user_credential = { instance, username, password, email ->
  // Set up the local user
  def hudsonRealm = new hudson.security.HudsonPrivateSecurityRealm(false)
  hudsonRealm.createAccount(username, password)
  instance.setSecurityRealm(hudsonRealm)
  def user = hudson.model.User.get(username)
  def email_param = new hudson.tasks.Mailer.UserProperty(email)
  user.addProperty(email_param)
  println "Creating ${username} user credentials"
}
