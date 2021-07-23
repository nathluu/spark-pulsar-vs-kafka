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
kubectl get secret pulsar-token-admin -n pulsar -o jsonpath='{.data}'
```

