package apache.activemq.example;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * http://activemq.apache.org/tcp-transport-reference.html
 * 
 * 61616
 */
public class HelloWorldProducer implements Runnable {

	// protected static String URL = "vm://localhost";
	// protected static String URL = "tcp://localhost:61616";
	protected static String URL = "tcp://localhost:8686";

	@Override
	public void run() {
		try {
			// Create a ConnectionFactory
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(URL);

			// Create a Connection
			Connection connection = connectionFactory.createConnection();
			connection.start();

			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			{
				// Create the destination (Topic or Queue)
				Destination destination = session.createQueue("TEST.FOO");

				// Create a MessageProducer from the Session to the Topic or Queue
				MessageProducer producer = session.createProducer(destination);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

				// Create a messages
				// String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
				String text = "Hello world! From: ABC";
				TextMessage message = session.createTextMessage(text);

				// Tell the producer to send the message
				// System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
				System.out.println("Sent message: \"Hello world! From: ABC\"");
				producer.send(message);

				producer.close();
			}

			// Clean up
			session.close();
			connection.close();

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldProducer(), false);
		Thread.sleep(1000);
	}

	public static void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}

}
