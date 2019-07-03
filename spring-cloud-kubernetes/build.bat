call mvn clean package -Dmaven.test.skip=true

cd travel-agency-service
docker build -t 172.16.10.177:5000/travel-agency-service .
cd ../client-service
docker build -t 172.16.10.177:5000/client-service .
cd ..

docker push 172.16.10.177:5000/travel-agency-service
docker push 172.16.10.177:5000/client-service