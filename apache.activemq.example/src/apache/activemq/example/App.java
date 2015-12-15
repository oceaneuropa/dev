package apache.activemq.example;

/**
 * ActiveMQ Hello World http://activemq.apache.org/hello-world.html
 * 
 * TCP transport http://activemq.apache.org/tcp-transport-reference.html
 * 
 */
public class App {

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldConsumer(), false);
		Thread.sleep(1000);

		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldProducer(), false);
		Thread.sleep(1000);

		// thread(new HelloWorldConsumer(), false);
		// thread(new HelloWorldProducer(), false);
		// thread(new HelloWorldConsumer(), false);
		// thread(new HelloWorldConsumer(), false);
		// thread(new HelloWorldProducer(), false);
		// thread(new HelloWorldProducer(), false);
		// Thread.sleep(1000);

		// thread(new HelloWorldProducer(), false);
		// thread(new HelloWorldConsumer(), false);
		// thread(new HelloWorldConsumer(), false);
		// thread(new HelloWorldProducer(), false);
		// thread(new HelloWorldConsumer(), false);
		// thread(new HelloWorldProducer(), false);
		// thread(new HelloWorldConsumer(), false);
		// thread(new HelloWorldProducer(), false);
		// thread(new HelloWorldConsumer(), false);
		// thread(new HelloWorldConsumer(), false);
		// thread(new HelloWorldProducer(), false);
	}

	/**
	 * 
	 * @param runnable
	 * @param daemon
	 */
	public static void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}

}