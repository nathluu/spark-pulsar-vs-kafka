import pulsar

client = pulsar.Client('pulsar://192.168.56.2:6650')

producer = client.create_producer('apache/pulsar/mytopic')

lines = ["Hello world", "Xin chao", "Bienvenue le monde"]

for line in lines:
    producer.send(line.encode('utf-8'))

client.close()
