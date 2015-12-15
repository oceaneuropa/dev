package apache.activemq.example;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class HelloWorldConsumer implements Runnable {

	// protected static String URL = "vm://localhost";
	// protected static String URL = "tcp://localhost:61616";
	protected static String URL = "tcp://localhost:8686";
	protected static boolean useMessageListener = true;

	@Override
	public void run() {
		try {
			// Create a ConnectionFactory
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(URL);
			Connection connection = connectionFactory.createConnection();
			connection.start();
			connection.setExceptionListener(new ExceptionListener() {
				@Override
				public void onException(JMSException arg0) {
					System.out.println("JMS Exception occured. Shutting down client.");
				}
			});

			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			{
				if (useMessageListener) {
					// Create the destination (Topic or Queue)
					Destination destination = session.createQueue("TEST.FOO");

					// Create a MessageConsumer from the Session to the Topic or Queue
					MessageConsumer consumer = session.createConsumer(destination);
					consumer.setMessageListener(new MessageListener() {
						@Override
						public void onMessage(Message message) {
							System.out.println("MessageListener.onMessage()");
							handleMessage(message);
						}
					});

				} else {
					// Create the destination (Topic or Queue)
					Destination destination = session.createQueue("TEST.FOO");

					// Create a MessageConsumer from the Session to the Topic or Queue
					MessageConsumer consumer = session.createConsumer(destination);

					// Wait for a message
					Message message = consumer.receive(1000);
					handleMessage(message);

					consumer.close();
				}
			}

			// Clean up
			// session.close();
			// connection.close();

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param message
	 */
	protected void handleMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
				System.out.println("Received Text: " + text);
			} else {
				System.out.println("Received Message: " + message);
			}
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
		if (useMessageListener) {
			thread(new HelloWorldConsumer(), false);

		} else {
			thread(new HelloWorldConsumer(), false);
			thread(new HelloWorldConsumer(), false);
			thread(new HelloWorldConsumer(), false);
			thread(new HelloWorldConsumer(), false);
			thread(new HelloWorldConsumer(), false);
			Thread.sleep(1000);
		}
	}

	public static void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		// brokerThread.setDaemon(daemon);
		brokerThread.start();
	}

}
