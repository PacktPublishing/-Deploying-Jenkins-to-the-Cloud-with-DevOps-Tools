# Jenkins on a Public Cloud (AWS)

## Setup

Requirements:
 * Ansible 2.5+
 * Python
 * AWS Account
```
sudo pip install -r requirements.txt  --ignore-installed six
ansible-galaxy install -r ansible/requirements.yml
aws configure                                                   # configures AWS CLI
ansible-playbook ansible/test.yml                               # test access
```

## Provision

```
ansible-playbook ansible/jenkins.yml
```

## SSH to master
```
ssh -i ansible/ssh/id_rsa centos@jenkins.cloudci.net
```

## Know issues

### DNS Lookup not working

The Route53 DNS propagation needs some time (10-15 minutes). Check if you can do a nslookup for your domain added (e.g. cloudci.net):
```
nslookup jenkins.cloudci.net
```
Wait until this command shows up an ip address, before rerun the playbook.

### Jenkins Master not reachable via SSH
```
fatal: [jenkins.cloudci.net]: UNREACHABLE! => {"changed": false, "msg": "SSH Error: data could not be sent to remote host \"jenkins.cloudci.net\". Make sure this host can be reached over ssh", "unreachable": true}
	to retry, use: --limit @/Users/packt/git/jenkins-packt-lab/Volume2/Section1_PublicCloud/ansible/jenkins.retry
```
To fix this, set the correct permissions:
```
chmod 0600 ansible/ssh/*
```