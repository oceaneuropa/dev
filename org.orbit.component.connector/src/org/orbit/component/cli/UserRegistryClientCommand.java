package org.orbit.component.cli;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier1.account.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.DateUtil;
import org.origin.common.util.PrettyPrinter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistryClientCommand implements CommandActivator {

	public static final String ID = "org.orbit.component.cli.UserRegistryClientCommand";

	protected static Logger LOG = LoggerFactory.getLogger(UserRegistryClientCommand.class);

	protected static String[] USER_ACCOUNT_COLUMNS = new String[] { "Account Id", "Username", "Email", "Password", "First Name", "Last Name", "Phone", "Activated", "Creation Time", "Last Update Time" };

	protected Map<Object, Object> properties;

	protected String getScheme() {
		return "orbit";
	}

	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function", new String[] { //
				"userregistry_ping", //
				"userregistry_echo", //
				"list_users", //
				"get_user", //
				"add_user", //
				"change_password", //
				"activate_user", //
				"deactivate_user", //
				"delete_user" //
		});

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_USER_ACCOUNTS_URL);
		this.properties = properties;

		OSGiServiceUtil.register(bundleContext, UserRegistryClientCommand.class.getName(), this, props);
	}

	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(UserRegistryClientCommand.class.getName(), this);
	}

	protected String getAccountId(String username) {
		String accountId = null;
		return accountId;
	}

	protected UserAccountClient getUserRegistry() {
		String userRegistryUrl = (String) this.properties.get(ComponentConstants.ORBIT_USER_ACCOUNTS_URL);
		UserAccountClient userRegistry = ComponentClientsUtil.UserAccounts.getUserAccountsClient(userRegistryUrl, null);
		if (userRegistry == null) {
			throw new IllegalStateException("UserRegistry is null.");
		}
		return userRegistry;
	}

	@Descriptor("list_users")
	public void list_users() throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "list_users");

		UserAccountClient userRegistry = getUserRegistry();

		UserAccount[] userAccounts = userRegistry.getUserAccounts();

		String[][] rows = new String[userAccounts.length][USER_ACCOUNT_COLUMNS.length];
		int rowIndex = 0;
		for (UserAccount currUserAccount : userAccounts) {
			String accountId = currUserAccount.getAccountId();
			String username = currUserAccount.getUsername();
			String email = currUserAccount.getEmail();
			String password = currUserAccount.getPassword();
			String firstName = currUserAccount.getFirstName();
			String lastName = currUserAccount.getLastName();
			String phone = currUserAccount.getPhone();
			boolean activated = currUserAccount.isActivated();
			Date createTime = currUserAccount.getCreationTime();
			Date updateTime = currUserAccount.getLastUpdateTime();
			String createTimeStr = (createTime != null) ? DateUtil.toString(createTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
			String updateTimeStr = (updateTime != null) ? DateUtil.toString(updateTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { accountId, username, email, password, firstName, lastName, phone, String.valueOf(activated), createTimeStr, updateTimeStr };
		}

		PrettyPrinter.prettyPrint(USER_ACCOUNT_COLUMNS, rows, userAccounts.length);
	}

	@Descriptor("get_user")
	public void get_user( //
			@Descriptor("username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "get_user", new String[] { "username", username });
		if (Parameter.UNSPECIFIED.equals(username)) {
			System.out.println("username is not set.");
			return;
		}

		String accountId = getAccountId(username);

		UserAccountClient userRegistry = getUserRegistry();

		UserAccount userAccount = userRegistry.getUserAccount(accountId);

		UserAccount[] userAccounts = (userAccount != null) ? new UserAccount[] { userAccount } : new UserAccount[0];
		String[][] rows = new String[userAccounts.length][USER_ACCOUNT_COLUMNS.length];
		int rowIndex = 0;
		for (UserAccount currUserAccount : userAccounts) {
			String currAccountId = currUserAccount.getAccountId();
			String currUsername = currUserAccount.getUsername();
			String firstName = currUserAccount.getFirstName();
			String lastName = currUserAccount.getLastName();
			String email = currUserAccount.getEmail();
			String phone = currUserAccount.getPhone();
			boolean activated = currUserAccount.isActivated();
			Date createTime = currUserAccount.getCreationTime();
			Date updateTime = currUserAccount.getLastUpdateTime();
			String createTimeStr = (createTime != null) ? DateUtil.toString(createTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
			String updateTimeStr = (updateTime != null) ? DateUtil.toString(updateTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { currAccountId, currUsername, email, firstName, lastName, phone, String.valueOf(activated), createTimeStr, updateTimeStr };
		}

		PrettyPrinter.prettyPrint(USER_ACCOUNT_COLUMNS, rows, userAccounts.length);
		System.out.println();
	}

	@Descriptor("add_user")
	public void add_user( //
			@Descriptor("URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username, //
			@Descriptor("Password") @Parameter(names = { "-password", "--password" }, absentValue = Parameter.UNSPECIFIED) String password, //
			@Descriptor("Email") @Parameter(names = { "-email", "--email" }, absentValue = Parameter.UNSPECIFIED) String email, //
			@Descriptor("First Name") @Parameter(names = { "-firstname", "--firstname" }, absentValue = Parameter.UNSPECIFIED) String firstName, //
			@Descriptor("Last Name") @Parameter(names = { "-lastname", "--lastname" }, absentValue = Parameter.UNSPECIFIED) String lastName, //
			@Descriptor("Phone") @Parameter(names = { "-phone", "--phone" }, absentValue = Parameter.UNSPECIFIED) String phone //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "add_user", //
				new String[] { "username", username }, //
				new String[] { "password", password }, //
				new String[] { "email", email }, //
				new String[] { "firstname", firstName }, //
				new String[] { "lastname", lastName }, //
				new String[] { "phone", phone } //
		);
		if (Parameter.UNSPECIFIED.equals(username)) {
			System.out.println("username is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(password)) {
			System.out.println("password is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(email)) {
			System.out.println("email is not set.");
			return;
		}

		UserAccountClient userRegistry = getUserRegistry();

		CreateUserAccountRequest request = new CreateUserAccountRequest();
		request.setUsername(username);
		request.setPassword(password);
		request.setEmail(email);
		request.setFirstName(firstName);
		request.setLastName(lastName);
		request.setPhone(phone);

		boolean succeed = userRegistry.register(request);
		System.out.println("User registration result: " + succeed);
	}

	@Descriptor("change_password")
	public void change_password( //
			@Descriptor("URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("UerId") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username, //
			@Descriptor("Old password") @Parameter(names = { "-oldpassword", "--oldpassword" }, absentValue = Parameter.UNSPECIFIED) String oldPassword, //
			@Descriptor("New password") @Parameter(names = { "-newpassword", "--newpassword" }, absentValue = Parameter.UNSPECIFIED) String newPassword //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "change_password", //
				new String[] { "username", username }, //
				new String[] { "oldpassword", oldPassword }, //
				new String[] { "newpassword", newPassword } //
		);
		if (Parameter.UNSPECIFIED.equals(username)) {
			System.out.println("username is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(newPassword)) {
			System.out.println("newpassword is not set.");
			return;
		}

		UserAccountClient userRegistry = getUserRegistry();

		boolean succeed = userRegistry.changePassword(username, oldPassword, newPassword);
		System.out.println("Password is updated: " + succeed);
	}

	@Descriptor("activate_user")
	public void activate_user( //
			@Descriptor("URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "activate_user", new String[] { "url", url }, new String[] { "username", username });
		if (Parameter.UNSPECIFIED.equals(username)) {
			System.out.println("username is not set.");
			return;
		}

		UserAccountClient userRegistry = getUserRegistry();

		boolean succeed = userRegistry.activate(username);
		boolean activated = userRegistry.isActivated(username);

		// System.out.println("User activation: " + succeed);
		System.out.println("User is activated: " + activated);
	}

	@Descriptor("deactivate_user")
	public void deactivate_user( //
			@Descriptor("URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "deactivate_user", new String[] { "url", url }, new String[] { "username", username });
		if (Parameter.UNSPECIFIED.equals(username)) {
			System.out.println("username is not set.");
			return;
		}

		UserAccountClient userRegistry = getUserRegistry();

		boolean succeed = userRegistry.deactivate(username);
		boolean activated = userRegistry.isActivated(username);

		// System.out.println("User deactivation: " + succeed);
		System.out.println("User is activated: " + activated);
	}

	@Descriptor("delete_user")
	public void delete_user( //
			@Descriptor("URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "remove_user", new String[] { "url", url }, new String[] { "username", username });
		if (Parameter.UNSPECIFIED.equals(username)) {
			System.out.println("username is not set.");
			return;
		}

		UserAccountClient userRegistry = getUserRegistry();

		boolean succeed = userRegistry.delete(username);
		System.out.println("User deletion result: " + succeed);
	}

}
