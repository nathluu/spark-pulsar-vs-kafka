**Start standalone pulsar broker using docker**

```bash
docker run -it -p 6650:6650 -p 8080:8080 --name normal --mount source=pulsardata,target=/pulsar/data   apachepulsar/pulsar:2.7.1 bin/pulsar standalone
```

**Start multi-brokers pulsar cluster using cloud Kubernetes service**

Deploy on Kubernetes

```bash
helm repo add apache https://pulsar.apache.org/charts
helm repo update
git clone https://github.com/apache/pulsar-helm-chart
cd pulsar-helm-chart
./scripts/pulsar/prepare_helm_release.sh \
    --namespace pulsar \
    --release pulsar \
    --create-namespace \
    --symmetric
cd ..
git clone https://github.com/nathluu/spark-pulsar-vs-kafka.git
cd spark-pulsar-vs-kafka
helm install \
    --values pulsar/examples-pulsar-chart-values/values-jwt-multiple-brokers.yaml \
    --set initialize=true \
    --namespace pulsar \
    pulsar apache/pulsar
```

Verify installed results

```bash
kubectl get pods -n pulsar
kubectl get services -n pulsar
kubectl get secret pulsar-token-admin -n pulsar -o jsonpath='{.data}' # base64 decode to get token
```

Istio

```bash
cd pulsar/istio
openssl req -x509 -sha256 -nodes -days 3650 -newkey rsa:2048 -subj '/O=TMA Inc./CN=TMA Root CA' -keyout tmanet.com.key -out tmanet.com.crt
openssl req -out app.tmanet.com.csr -newkey rsa:2048 -nodes -keyout app.tmanet.com.key -subj "/CN=*.tmanet.com/O=TMA"
openssl x509 -req -days 365 -CA tmanet.com.crt -CAkey tmanet.com.key -set_serial 0 -in app.tmanet.com.csr -out app.tmanet.com.crt

kubectl create -n istio-system secret tls ca-key-pair --key=app.tmanet.com.key --cert=app.tmanet.com.crt
kubectl apply -f istio-gw-vs.yaml -n pulsar
```