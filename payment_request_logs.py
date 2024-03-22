import pika

connection = pika.BlockingConnection()
channel = connection.channel()

for method_frame, properties, body in channel.consume('payments-requests'):
    # Display the message parts and acknowledge the message
    print(body)
    channel.basic_ack(method_frame.delivery_tag)

# Cancel the consumer and return any pending messages
requeued_messages = channel.cancel()
print('Requeued %i messages' % requeued_messages)
connection.close()