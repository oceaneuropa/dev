package apache.activemq.producer.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ExampleProducer {

	protected static String URL = "tcp://localhost:8686";
	protected static String QUEUE_NAME = "TEST.QUEUE";

	protected ActiveMQConnectionFactory connectionFactory;

	public ExampleProducer() {
		this.connectionFactory = new ActiveMQConnectionFactory(URL);
	}

	protected HashMap<String, Object> createRequest1() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		{
			map.put("requestType", "action1");
		}
		{
			HashMap<String, Object> props = new HashMap<String, Object>();
			props.put("org", "org1");
			props.put("id", "id1");
			map.put("requestProperties", props);
		}
		return map;
	}

	protected HashMap<String, Object> createRequest2() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		{
			map.put("requestType", "action2");
		}
		{
			HashMap<String, Object> props = new HashMap<String, Object>();
			props.put("org", "org2");
			props.put("id", "id2");
			map.put("requestProperties", props);
		}
		return map;
	}

	public void start() {
		System.out.println("ExampleProducer.start()");

		sendObjectMessage(QUEUE_NAME, createRequest1());
		sendBytesMessage(QUEUE_NAME, createRequest2());
	}

	/**
	 * 
	 * @param queueName
	 * @param messagePayload
	 */
	public void sendObjectMessage(String queueName, Serializable messagePayload) {
		System.out.println("ExampleProducer.sendObjectMessage() -> " + messagePayload.toString());

		try {
			Connection connection = this.connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(queueName);

			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);

			ObjectMessage objectMessage = session.createObjectMessage(messagePayload);
			producer.send(objectMessage);

			producer.close();
			session.close();
			connection.close();

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param queueName
	 * @param messagePayload
	 */
	public void sendBytesMessage(String queueName, Serializable messagePayload) {
		System.out.println("ExampleProducer.sendBytesMessage() -> " + messagePayload.toString());

		try {
			Connection connection = this.connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(queueName);

			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);

			ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
			ObjectOutputStream objOS = new ObjectOutputStream(byteOS);
			objOS.writeObject(messagePayload);
			byte[] bytes = byteOS.toByteArray();

			BytesMessage bytesMessage = session.createBytesMessage();
			bytesMessage.writeBytes(bytes);
			producer.send(bytesMessage);

			byteOS.close();
			objOS.close();

			producer.close();
			session.close();
			connection.close();

		} catch (JMSException | IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		System.out.println("ExampleProducer.stop()");
	}

}
