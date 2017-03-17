package com.learn.rabbitMQ.publish;

import java.io.IOException;
import java.time.LocalDateTime;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

	private final static String QUEUE_NAME="hello";
	
	public static void main(String[] argv) throws IOException{
		// Create a connection to the server
		try{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost"); // connection to broker at local machine
		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		String msg = "Hello World!";
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		System.out.println(" [*] Sent ' "+msg+" ' at "+LocalDateTime.now());
		channel.close();
		conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
