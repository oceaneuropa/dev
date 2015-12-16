package apache.activemq.example;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicProducer implements Runnable {

	@Override
	public void run() {
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:8686");

			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic("TOPIC.FOO");

			MessageProducer producer = session.createProducer(topic);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			String text = "Hello world! From: XYZ";
			TextMessage message = session.createTextMessage(text);
			System.out.println("Sent message: \"Hello world! From: XYZ\"");
			producer.send(message);

			producer.close();
			session.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		thread(new TopicProducer(), false);
		thread(new TopicProducer(), false);
		thread(new TopicProducer(), false);
		thread(new TopicProducer(), false);
		Thread.sleep(1000);
	}

	public static void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}

}