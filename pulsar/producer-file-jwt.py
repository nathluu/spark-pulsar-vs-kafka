import sys, time, argparse
import pulsar

def main(argv):
    parser = argparse.ArgumentParser(description='Publish file content line by line to pulsar broker')
    parser.add_argument('--serviceUrl', default='pulsar://51.143.57.135:6650', help='Pulsar service URL')
    parser.add_argument('--topic', default='apache/pulsar/mytopic', help='Publish topic')
    parser.add_argument('-f','--file', help='File to publish')

    args = parser.parse_args()
    client = pulsar.Client(args.serviceUrl, authentication=pulsar.AuthenticationToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiJ9.BzjL7d2zY0fRMy6_LmGi7tMikBMYxr7J8QsBroHWJTo"))
    producer = client.create_producer(args.topic)
    
    with open(args.file) as file:
        for line in file:
            print(line)
            producer.send(line.encode('utf-8'))
            time.sleep(5)

    client.close()

if __name__ == "__main__":
   main(sys.argv[1:])
