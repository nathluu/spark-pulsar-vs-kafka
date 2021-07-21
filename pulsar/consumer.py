import pulsar

client = pulsar.Client('pulsar://192.168.56.2:6650')

consumer = client.subscribe('apache/pulsar/mytopic1', 'my-subscription')

while True:
    msg = consumer.receive()
    try:
        print("Received message '{}' id='{}'".format(msg.data(), msg.message_id()))
        # Acknowledge successful processing of the message
        consumer.acknowledge(msg)
    except:
        # Message failed to be processed
        consumer.negative_acknowledge(msg)

client.close()
