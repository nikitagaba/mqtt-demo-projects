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

public class Recv {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws IOException, InterruptedException {
		// Create a connection to the server
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost"); // connection to broker at local
											// machine
			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out.println(" [*] Waiting for msgs. To exit press CTRL+C");
			Consumer consumer = new DefaultConsumer(channel) {

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					String msg = new String(body, "UTF-8");
					System.out.println("[x] Recieved: ' " + msg + " 'at "+LocalDateTime.now());
				}
			};

			channel.basicConsume(QUEUE_NAME, true, consumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
