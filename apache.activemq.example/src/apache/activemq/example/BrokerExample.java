package apache.activemq.example;

import java.io.IOException;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.PersistenceAdapter;
import org.apache.activemq.store.jdbc.JDBCPersistenceAdapter;
import org.apache.derby.jdbc.EmbeddedDataSource;

/**
 * 1. Embedded activemq
 * 
 * (1) http://activemq.apache.org/how-do-i-embed-a-broker-inside-a-connection.html
 * 
 * (2) http://stackoverflow.com/questions/14480023/activemq-and-embedded-broker
 * 
 * 2. PersistenceAdapter
 * 
 * (1) http://www.programcreek.com/java-api-examples/index.php?api=org.apache.activemq.store.jdbc.JDBCPersistenceAdapter
 *
 */
public class BrokerExample {

	// protected static String SERVER_URL = "tcp://localhost:51616";
	protected static String SERVER_URL = "tcp://localhost:8686";

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BrokerService broker = new BrokerService();

			// KahaPersistenceAdapter adaptor = new KahaPersistenceAdapter();
			// adaptor.setDirectory(new File("activemq"));
			// broker.setPersistenceAdapter(adaptor);

			// PersistenceAdapter adapter = createPersistenceAdapter();
			// broker.setPersistenceAdapter(adapter);

			broker.setUseJmx(true);
			broker.addConnector(SERVER_URL);
			broker.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Example 1
	 * 
	 * http://www.programcreek.com/java-api-examples/index.php?api=org.apache.activemq.store.jdbc.JDBCPersistenceAdapter
	 * 
	 * @return
	 * @throws IOException
	 */
	protected static PersistenceAdapter createPersistenceAdapter() throws IOException {
		JDBCPersistenceAdapter jdbc = new JDBCPersistenceAdapter();
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName("derbyDb");
		dataSource.setCreateDatabase("create");
		jdbc.setDataSource(dataSource);
		// jdbc.setCleanupPeriod(1000);
		return jdbc;
	}

}
