package com.learn.rabbitMQ.publish;

import java.io.IOException;
import java.time.LocalDateTime;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NewTaskSend {

	private final static String QUEUE_NAME="task_queue";
	
	public static void main(String[] argv) throws IOException{
		// Create a connection to the server
		try{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost"); // connection to broker at local machine
		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		// In order to make sure the RabbitMQ Server never lose the queues
		// or messages when it crashed , we need to mark both queue and the
		// message as durable.
		boolean durable = true;
		// marking queue as durable
		channel.queueDeclare(QUEUE_NAME,durable,false,false,null);
		String msg = getMessage(argv);
		// adding persistent property to message -> this tells RabbitMQ to save the message to disk.
		channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
		System.out.println(" [*] Sent ' "+msg+" ' at "+LocalDateTime.now());
		channel.close();
		conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private static String getMessage(String[] argv) {
		// TODO Auto-generated method stub
		if(argv.length < 1){
			return "Hello World!";
		}
		return joinStrings(argv," ");
	}

	private static String joinStrings(String[] argv, String string) {
		// TODO Auto-generated method stub
		int len = argv.length;
		if(len == 0) return "";
		StringBuilder words = new StringBuilder(argv[0]);
		for(int i = 1;i<len;i++){
			words.append(string).append(argv[i]);
		}
		return words.toString();
	}
	
}
