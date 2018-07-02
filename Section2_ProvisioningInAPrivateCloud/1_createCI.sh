#!/usr/bin/env bash
#
# Setup preconfigured CI Environment on Kubernetes

# create namespace
kubectl create ns ci

# Registry
kubectl create -f config/ci/registry/

# Gitlab
kubectl create -f config/ci/gitlab/
kubectl create -f config/ci/gitlab/lb/

# Jenkins
kubectl create secret generic jenkins-tls --from-file=config/ci/jenkins/tls.crt --from-file=config/ci/jenkins/tls.key --namespace ci
kubectl create secret generic jenkins --from-file=config/ci/jenkins/options --namespace=ci
kubectl apply -f config/ci/jenkins/
kubectl apply -f config/ci/jenkins/lb/

# wait for completion
sleep 60s
./bin/wait.sh gitlab-postgresql
./bin/wait.sh gitlab
./bin/wait.sh jenkins
sleep 60s

echo "=> Preparing Data ..."
# unzip
unzip config/setup.zip -d . > /dev/null
# prepare data
jenkins_pod="$(kubectl get pods --selector=app=jenkins | tail -n +2 | awk '{print $1}')"
kubectl config view --flatten --minify > ./tmp/jenkins/jenkins_home/kube.config
kubectl cp ./tmp/jenkins/jenkins_home $jenkins_pod:/var/
gitlabdb_pod="$(kubectl get pods --selector=app=postgresql | tail -n +2 | awk '{print $1}')"
kubectl cp ./tmp/gitlab-db/main $gitlabdb_pod:/var/lib/postgresql/9.6
gitlab_pod="$(kubectl get pods --selector=app=gitlab | tail -n +2 | awk '{print $1}')"
kubectl cp ./tmp/gitlab/data $gitlab_pod:/home/git
# clean up
rm -rf ./tmp

echo "=> Restarting Pods ..."
# force restart of pods
kubectl exec $gitlabdb_pod -c postgresql /sbin/killall5
kubectl exec $gitlab_pod -c gitlab /sbin/killall5
kubectl exec $jenkins_pod -c master /sbin/killall5

# set default namespace to jenkins
kubectl config set-context $(kubectl config current-context) --namespace=ci
