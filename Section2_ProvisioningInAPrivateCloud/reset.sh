#!/usr/bin/env bash
#
# Reset environments

kubectl delete ns ci --ignore-not-found=true --now=true -R --timeout=120s
kubectl delete ns jenkins --ignore-not-found=true --now=true -R --timeout=120s