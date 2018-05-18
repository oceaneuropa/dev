package org.origin.common.launch.util;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SocketUtil {

	private static final Random random = new Random(System.currentTimeMillis());

	/**
	 * Returns a free port number on the specified host within the given range, or -1 if none found.
	 * 
	 * @param host
	 *            name or IP address of host on which to find a free port
	 * @param searchFrom
	 *            the port number from which to start searching
	 * @param searchTo
	 *            the port number at which to stop searching
	 * @return a free port in the specified range, or -1 of none found
	 * @deprecated Use <code>findFreePort()</code> instead. It is possible that this method can return a port that is already in use since the implementation
	 *             does not bind to the given port to ensure that it is free.
	 */
	public static int findUnusedLocalPort(String host, int searchFrom, int searchTo) {
		for (int i = 0; i < 10; i++) {
			Socket s = null;
			int port = getRandomPort(searchFrom, searchTo);
			try {
				s = new Socket(host, port);
			} catch (ConnectException e) {
				return port;
			} catch (IOException e) {
			} finally {
				if (s != null) {
					try {
						s.close();
					} catch (IOException ioe) {
					}
				}
			}
		}
		return -1;
	}

	private static int getRandomPort(int low, int high) {
		return (int) (random.nextFloat() * (high - low)) + low;
	}

	/**
	 * Returns a free port number on localhost, or -1 if unable to find a free port.
	 * 
	 */
	public static int findFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			return socket.getLocalPort();

		} catch (IOException e) {
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		return -1;
	}

}
