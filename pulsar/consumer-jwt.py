import pulsar
from pulsar import MessageId

client = pulsar.Client('pulsar://51.143.57.135:6650', authentication=pulsar.AuthenticationToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiJ9.BzjL7d2zY0fRMy6_LmGi7tMikBMYxr7J8QsBroHWJTo"))

consumer = client.subscribe('apache/pulsar/mytopic1', 'my-subscription')
consumer.seek(MessageId.latest)

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
