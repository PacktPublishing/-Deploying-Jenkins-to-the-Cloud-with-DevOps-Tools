# Kubernetes & Jenkins

## Troubleshooting

### Not enough resources
Error regarding resources, e.g. memory.

Increase minikube resources:
```
minikube stop
minikube delete 
minikube stop--cpus 4 --memory 8192
```

### VirtualBox issues on mac
start on mac without VirtualBox
```
curl -LO https://storage.googleapis.com/minikube/releases/latest/docker-machine-driver-hyperkit \
&& chmod +x docker-machine-driver-hyperkit \
&& sudo mv docker-machine-driver-hyperkit /usr/local/bin/ \
&& sudo chown root:wheel /usr/local/bin/docker-machine-driver-hyperkit \
&& sudo chmod u+s /usr/local/bin/docker-machine-driver-hyperkit
minikube stop
minikube delete
minikube start --cpus 4 --memory 8192 --vm-driver=hyperkit
```