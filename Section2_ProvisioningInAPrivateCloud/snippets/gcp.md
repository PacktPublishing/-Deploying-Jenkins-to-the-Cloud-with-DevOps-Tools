# Connect to Google Compute Engine Kubernetes via kubectl

```
gcloud auth login

gcloud container clusters get-credentials cluster-1 --zone us-central1-a --project packt-course-191916

kubectl proxy

open http://127.0.0.1:8001/ui/ 

kubectl config view | grep access-token

# can now run ./1_createCI.sh

```