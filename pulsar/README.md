Start standalone pulsar broker using docker

`docker run -it -p 6650:6650 -p 8080:8080 --name normal --mount source=pulsardata,target=/pulsar/data   apachepulsar/pulsar:2.7.1 bin/pulsar standalone`

