kubectl delete -f travel-agency-service/secret.yaml
kubectl delete -f travel-agency-service/mongo-deployment.yaml
kubectl create -f travel-agency-service/secret.yaml
kubectl create -f travel-agency-service/mongo-deployment.yaml
kubectl delete -f travel-agency-service/travel-agency-deployment.yaml
kubectl create -f travel-agency-service/travel-agency-deployment.yaml
kubectl delete configmap client-service
kubectl delete -f client-service/client-service-deployment.yaml
kubectl create -f client-service/client-config.yaml
kubectl create -f client-service/client-service-deployment.yaml
kubectl get pods