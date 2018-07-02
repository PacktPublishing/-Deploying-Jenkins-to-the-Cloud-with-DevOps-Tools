#!/usr/bin/env bash

kubectl create ns jenkins
kubectl apply -f config/minikube/
kubectl apply -f config/minikube/lb/

# set default namespace to jenkins
kubectl config set-context $(kubectl config current-context) --namespace=jenkins
