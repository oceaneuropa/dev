package apache.activemq.example;

import java.io.IOException;
import java.util.Properties;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.PersistenceAdapter;
import org.apache.activemq.store.jdbc.JDBCPersistenceAdapter;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.hsqldb.jdbc.JDBCDataSource;

public class PersistenceAdapterExample {

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
		jdbc.setCleanupPeriod(1000);
		return jdbc;
	}

	/**
	 * Example 2
	 * 
	 * @return
	 * @throws IOException
	 */
	protected void createMaster() throws Exception {
		BrokerService master = new BrokerService();
		master.setBrokerName("master");
		// master.addConnector(MASTER_URL);
		master.setUseJmx(false);
		master.setPersistent(true);
		master.setDeleteAllMessagesOnStartup(true);
		JDBCPersistenceAdapter persistenceAdapter = new JDBCPersistenceAdapter();
		// persistenceAdapter.setDataSource(getExistingDataSource());
		// configureJdbcPersistenceAdapter(persistenceAdapter);
		master.setPersistenceAdapter(persistenceAdapter);
		configureBroker(master);
		master.start();
	}

	/**
	 * Example 3
	 * 
	 * @return
	 * @throws IOException
	 */
	protected void configureBroker(BrokerService broker) throws Exception {
		// super.configureBroker(broker);
		JDBCPersistenceAdapter jdbc = new JDBCPersistenceAdapter();
		// jdbc.setDataSource(dataSource);
		broker.setPersistenceAdapter(jdbc);
	}

	/**
	 * Example 4
	 * 
	 * @throws Exception
	 */
	public void testConfiguredCorrectly() throws Exception {
		// PersistenceAdapter persistenceAdapter = brokerService.getPersistenceAdapter();
		// assertNotNull(persistenceAdapter);
		// assertTrue(persistenceAdapter instanceof JDBCPersistenceAdapter);
		// JDBCPersistenceAdapter jpa = (JDBCPersistenceAdapter) persistenceAdapter;
		// assertEquals("BROKER1.", jpa.getStatements().getTablePrefix());
	}

	/**
	 * Example 7
	 * 
	 * @return
	 * @throws Exception
	 */
	protected BrokerService createBroker() throws Exception {
		BrokerService broker = new BrokerService();
		broker.setOfflineDurableSubscriberTaskSchedule(500);
		broker.setOfflineDurableSubscriberTimeout(2000);
		JDBCPersistenceAdapter jdbc = new JDBCPersistenceAdapter();
		// EmbeddedDataSource dataSource = new EmbeddedDataSource();
		// dataSource.setDatabaseName("derbyDb");
		// dataSource.setCreateDatabase("create");
		// jdbc.setDataSource(dataSource);
		jdbc.deleteAllMessages();
		broker.setPersistenceAdapter(jdbc);
		return broker;
	}

	/**
	 * Example 12
	 * 
	 * @throws Exception
	 */
	public void testDirectDataSource() throws Exception {
		Properties properties = new Properties();
		JDBCDataSource dataSource = new JDBCDataSource();
		dataSource.setDatabase("jdbc:hsqldb:mem:testdb" + System.currentTimeMillis());
		dataSource.setUser("sa");
		dataSource.setPassword("");
		dataSource.getConnection().close();
		properties.put("DataSource", dataSource);
		properties.put("UseDatabaseLock", "false");
		properties.put("StartupTimeout", "10000");
		// ActiveMQFactory.setThreadProperties(properties);
		BrokerService broker = null;
		try {
			// broker = BrokerFactory.createBroker(new URI(getBrokerUri("broker:(tcp://localhost:" + brokerPort + ")?useJmx=false")));
			// assertNotNull("broker is null", broker);
			PersistenceAdapter persistenceAdapter = broker.getPersistenceAdapter();
			// assertNotNull("persistenceAdapter is null", persistenceAdapter);
			// assertTrue("persistenceAdapter should be an instance of JDBCPersistenceAdapter", persistenceAdapter instanceof JDBCPersistenceAdapter);
			JDBCPersistenceAdapter jdbcPersistenceAdapter = (JDBCPersistenceAdapter) persistenceAdapter;
			// assertSame(dataSource, jdbcPersistenceAdapter.getDataSource());
		} finally {
			// stopBroker(broker);
			// ActiveMQFactory.setThreadProperties(null);
		}
	}

	/**
	 * Example 13
	 * 
	 * @throws Exception
	 */
	public void testLookupDataSource() throws Exception {
		final Properties properties = new Properties();
		final JDBCDataSource dataSource = new JDBCDataSource();
		dataSource.setDatabase("jdbc:hsqldb:mem:testdb" + System.currentTimeMillis());
		dataSource.setUser("sa");
		dataSource.setPassword("");
		dataSource.getConnection().close();

		// MockInitialContextFactory.install(Collections.singletonMap("openejb/Resource/TestDs", dataSource));
		// assertSame(dataSource, new InitialContext().lookup("openejb/Resource/TestDs"));

		// CoreContainerSystem containerSystem = new CoreContainerSystem(new IvmJndiFactory());
		// containerSystem.getJNDIContext().bind("openejb/Resource/TestDs", dataSource);

		// SystemInstance.get().setComponent(ContainerSystem.class, containerSystem);
		properties.put("DataSource", "TestDs");
		properties.put("UseDatabaseLock", "false");
		properties.put("StartupTimeout", "10000");

		// ActiveMQFactory.setThreadProperties(properties);
		BrokerService broker = null;
		try {
			// broker = BrokerFactory.createBroker(new URI(getBrokerUri("broker:(tcp://localhost:" + brokerPort + ")?useJmx=false")));
			// assertNotNull("broker is null", broker);
			PersistenceAdapter persistenceAdapter = broker.getPersistenceAdapter();
			// assertNotNull("persistenceAdapter is null", persistenceAdapter);
			// assertTrue("persistenceAdapter should be an instance of JDBCPersistenceAdapter", persistenceAdapter instanceof JDBCPersistenceAdapter);
			JDBCPersistenceAdapter jdbcPersistenceAdapter = (JDBCPersistenceAdapter) persistenceAdapter;
			// assertSame(dataSource, jdbcPersistenceAdapter.getDataSource());
		} finally {
			// stopBroker(broker);
			// ActiveMQFactory.setThreadProperties(null);
		}
	}

	/**
	 * Example 17
	 * 
	 * @param deleteMessages
	 * @throws Exception
	 */
	private void createBroker(boolean deleteMessages) throws Exception {
		BrokerService broker = new BrokerService();
		broker.setDeleteAllMessagesOnStartup(deleteMessages);
		broker.setPersistenceAdapter(new JDBCPersistenceAdapter());
		broker.setAdvisorySupport(false);
		broker.addConnector("tcp://0.0.0.0:0");
		broker.start();
		broker.waitUntilStarted();
		String connectionUri = broker.getTransportConnectors().get(0).getPublishableConnectString();
		// ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(connectionUri);
	}

}
