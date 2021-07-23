import pulsar

client = pulsar.Client('pulsar://pulsar.abcd.com:80', authentication=pulsar.AuthenticationToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiJ9.SaMEgByUS3P7UVCJi5NCHOF5kMP6_tM0Vl4IPlY1-5E"))

producer = client.create_producer('apache/pulsar/mytopic')

for i in range(10):
    producer.send(('Hello world humana %d' % i).encode('utf-8'))

client.close()
