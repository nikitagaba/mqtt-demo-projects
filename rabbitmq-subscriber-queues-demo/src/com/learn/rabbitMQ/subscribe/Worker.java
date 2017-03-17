package com.learn.rabbitMQ.subscribe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Stack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Worker {
	private final static String QUEUE_NAME = "task_queue";

	public static void main(String[] argv) throws IOException, InterruptedException {
		// Create a connection to the server
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost"); // connection to broker at local
											// machine
			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();
			boolean durable = true;
			// marking queue as durable
			channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
			System.out.println(" [*] Waiting for msgs. To exit press CTRL+C");
			
			// Setting prefetchCount to 1 does not dispatch a new message to a worker until it has
			// processed and acknowledged the previous one. Instead, it will
			// dispatch it to the next worker that is not still busy.
			int prefetchCount = 1;
			channel.basicQos(prefetchCount);
			Consumer consumer = new DefaultConsumer(channel) {

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					String msg = new String(body, "UTF-8");
					System.out.println("[x] Recieved: ' " + msg + " 'at " + LocalDateTime.now());
					try {
						doWork(msg);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						System.out.println(" [x] Done. ");
						channel.basicAck(envelope.getDeliveryTag(), false);
					}
				}
			};
			// Ack is true by default, if no ack is required , change autoAck to
			// True.
			boolean autoAck = false;
			channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doWork(String msg) throws InterruptedException {
		// TODO Auto-generated method stub
		for (char ch : msg.toCharArray()) {
			if (ch == '.')
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
		}
	}
}
