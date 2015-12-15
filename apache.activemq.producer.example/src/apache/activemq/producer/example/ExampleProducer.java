package apache.activemq.producer.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

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

		new Thread(new Runnable() {
			@Override
			public void run() {
				sendObjectMessage(QUEUE_NAME, createRequest1());
				sendBytesMessage(QUEUE_NAME, createRequest2());
			}
		}).start();
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

	/**
	 * 
	 * @param queueName
	 * @param messagePayload
	 */
	public void sendObjectMessage(String queueName, Serializable messagePayload) {
		System.out.println("ExampleProducer.sendRequestMessage() -> " + messagePayload.toString());

		try {
			Connection connection = this.connectionFactory.createConnection();
			connection.start();

			// request
			Session requestSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue requestQueue = requestSession.createQueue(queueName);
			MessageProducer requestProducer = requestSession.createProducer(requestQueue);
			requestProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

			// reply
			Session replySession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			TemporaryQueue replyQueue = replySession.createTemporaryQueue();
			MessageConsumer replyConsumer = replySession.createConsumer(replyQueue);

			// create message
			ObjectMessage requestMessage = createObjectMessage(requestSession, messagePayload);
			requestMessage.setJMSReplyTo(replyQueue);

			// send
			requestProducer.send(requestMessage);

			// get reply
			Message replyMessage = replyConsumer.receive(3 * 1000);
			handleMessage(replyMessage);

			// close
			replyConsumer.close();
			replyQueue.delete();
			replySession.close();

			requestProducer.close();
			requestSession.close();

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
		System.out.println("ExampleProducer.sendRequestMessage() -> " + messagePayload.toString());

		try {
			Connection connection = this.connectionFactory.createConnection();
			connection.start();

			// request
			Session requestSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue requestQueue = requestSession.createQueue(queueName);
			MessageProducer requestProducer = requestSession.createProducer(requestQueue);
			requestProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

			// reply
			Session replySession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			TemporaryQueue replyQueue = replySession.createTemporaryQueue();
			MessageConsumer replyConsumer = replySession.createConsumer(replyQueue);

			// create message
			BytesMessage bytesMessage = createBytesMessage(requestSession, messagePayload);
			bytesMessage.setJMSReplyTo(replyQueue);

			// send request
			requestProducer.send(bytesMessage);

			// get reply
			Message replyMessage = replyConsumer.receive(10 * 1000);
			handleMessage(replyMessage);

			// close
			replyConsumer.close();
			replyQueue.delete();
			replySession.close();

			requestProducer.close();
			requestSession.close();

			connection.close();

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param message
	 */
	public void handleMessage(Message message) {
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
	}

	/**
	 * 
	 * @param objectMessage
	 */
	public void handleObjectMessage(ObjectMessage objectMessage) {
		try {
			Serializable payload = objectMessage.getObject();
			System.out.println("ExampleProducer.getReplyMessage() -> " + payload.toString());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param bytesMessage
	 */
	public void handleBytesMessage(BytesMessage bytesMessage) {
		try {
			long length = bytesMessage.getBodyLength();
			byte[] bytes = new byte[(int) length];
			bytesMessage.readBytes(bytes);

			ByteArrayInputStream byteIS = new ByteArrayInputStream(bytes);
			ObjectInputStream objIS = new ObjectInputStream(byteIS);

			Serializable payload = (Serializable) objIS.readObject();
			System.out.println("ExampleProducer.getReplyMessage() -> " + payload.toString());

		} catch (JMSException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		System.out.println("ExampleProducer.stop()");
	}

}
