package apache.activemq.consumer.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ExampleConsumer {

	protected static String URL = "tcp://localhost:8686";
	protected static String QUEUE_NAME = "TEST.QUEUE";

	protected ActiveMQConnectionFactory connectionFactory;
	protected Connection connection;
	protected Session session;
	protected MessageConsumer consumer;

	public ExampleConsumer() {
		this.connectionFactory = new ActiveMQConnectionFactory(URL);
	}

	protected HashMap<String, Object> createResponse1() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		{
			map.put("responseCode", "200");
		}
		{
			HashMap<String, Object> props = new HashMap<String, Object>();
			props.put("status", "success");
			props.put("detail", "job is done");
			map.put("responseProperties", props);
		}
		return map;
	}

	protected HashMap<String, Object> createResponse2() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		{
			map.put("responseCode", "201");
		}
		{
			HashMap<String, Object> props = new HashMap<String, Object>();
			props.put("status", "failed");
			props.put("detail", "job is cancelled");
			map.put("responseProperties", props);
		}
		return map;
	}

	/**
	 * 
	 * @param session
	 * @param payload
	 * @return
	 */
	public ObjectMessage createObjectMessage(Session session, Serializable payload) {
		ObjectMessage requestMessage = null;
		try {
			requestMessage = session.createObjectMessage(payload);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return requestMessage;
	}

	/**
	 * 
	 * @param session
	 * @param payload
	 * @return
	 */
	public BytesMessage createBytesMessage(Session session, Serializable payload) {
		BytesMessage bytesMessage = null;
		try {
			ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
			ObjectOutputStream objOS = new ObjectOutputStream(byteOS);
			objOS.writeObject(payload);

			byte[] bytes = byteOS.toByteArray();

			bytesMessage = session.createBytesMessage();
			bytesMessage.writeBytes(bytes);

			byteOS.close();
			objOS.close();

		} catch (JMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytesMessage;
	}

	public void start() {
		System.out.println("ExampleConsumer.start()");

		try {
			this.connection = this.connectionFactory.createConnection();
			this.connection.start();

			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(QUEUE_NAME);

			this.consumer = session.createConsumer(destination);
			this.consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					handleRequestMessage(message);
					sendResponseMessage(session, message);
				}
			});

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param message
	 */
	public void handleRequestMessage(Message message) {
		if (message instanceof ObjectMessage) {
			handleObjectMessage((ObjectMessage) message);

		} else if (message instanceof BytesMessage) {
			handleBytesMessage((BytesMessage) message);

		} else {
			if (message == null) {
				System.out.println("###### Unhandled jms message: null");
			} else {
				System.out.println("###### Unhandled jms message: " + message.getClass().getName());
			}
		}

		if (message != null) {
			// acknowledge the message receipt
			try {
				message.acknowledge();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param objectMessage
	 */
	public void handleObjectMessage(ObjectMessage objectMessage) {
		System.out.print("ExampleConsumer.getRequestMessage() -> ");

		try {
			Serializable messagePayload = objectMessage.getObject();
			System.out.println(messagePayload.toString());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param bytesMessage
	 */
	public void handleBytesMessage(BytesMessage bytesMessage) {
		System.out.print("ExampleConsumer.getRequestMessage() -> ");

		try {
			long length = bytesMessage.getBodyLength();
			byte[] bytes = new byte[(int) length];
			bytesMessage.readBytes(bytes);

			ByteArrayInputStream byteIS = new ByteArrayInputStream(bytes);
			ObjectInputStream objIS = new ObjectInputStream(byteIS);

			Serializable messagePayload = (Serializable) objIS.readObject();
			System.out.println(messagePayload.toString());

		} catch (JMSException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param session
	 * @param requestMessage
	 */
	public void sendResponseMessage(Session session, Message requestMessage) {
		if (requestMessage == null) {
			return;
		}

		try {
			// response
			Destination responseQueue = requestMessage.getJMSReplyTo();
			MessageProducer responseProducer = session.createProducer(responseQueue);

			// create message
			Message responseMessage = null;
			if (requestMessage instanceof ObjectMessage) {
				Serializable payload = createResponse1();
				System.out.println("ExampleConsumer.sendResponseMessage() -> " + payload.toString());
				responseMessage = createObjectMessage(session, payload);

			} else if (requestMessage instanceof BytesMessage) {
				Serializable payload = createResponse2();
				System.out.println("ExampleConsumer.sendResponseMessage() -> " + payload.toString());
				responseMessage = createBytesMessage(session, payload);
			}

			// send response
			responseProducer.send(responseMessage);

			// close
			responseProducer.close();

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		System.out.println("ExampleConsumer.stop()");
		try {
			if (this.consumer != null) {
				this.consumer.close();
			}

			if (this.session != null) {
				this.session.close();
			}

			if (this.connection != null) {
				this.connection.close();
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
