package org.origin.common.util;

public class CLIHelper {

	private static CLIHelper INSTANCE = new CLIHelper();

	public static CLIHelper getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param scheme
	 * @param command
	 * @param params
	 */
	public void printCommand(String scheme, String command, String[]... params) {
		System.out.println("--------------------------------------------------------------");

		System.out.println("command:");
		if (scheme != null) {
			command = scheme + ":" + command;
		}
		System.out.println("    " + command);

		System.out.println("parameters:");
		if (params != null && params.length > 0) {
			for (String[] param : params) {
				String paramName = param[0];
				String paramValue = param[1];

				if ("org.apache.felix.service.command.unspecified.parameter".equals(paramValue)) {
					paramValue = "(n/a)";
				}

				if ("n/a".equals(paramName)) {
					System.out.println("    " + paramName);
					continue;
				}

				if (paramName != null && !paramName.startsWith("-")) {
					paramName = "-" + paramName;
				}
				System.out.println("    " + paramName + " = " + paramValue);
			}
		} else {
			System.out.println("    n/a");
		}

		System.out.println("--------------------------------------------------------------");
		System.out.println();
	}

}
