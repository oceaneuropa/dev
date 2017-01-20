package org.origin.common.resource.impl;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.origin.common.resource.URIConverter;
import org.origin.common.resource.URIHandler;

/**
 * @see org.eclipse.emf.ecore.resource.URIHandler
 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class URIHandlerImpl implements URIHandler {

	/**
	 * 
	 * @param options
	 * @return
	 */
	protected int getTimeout(Map<?, ?> options) {
		Integer timeout = (Integer) options.get(URIConverter.OPTION_TIMEOUT);
		return timeout == null ? 0 : timeout.intValue();
	}

	/**
	 * 
	 * @param options
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<Object, Object> getResponse(Map<?, ?> options) {
		return (Map<Object, Object>) options.get(URIConverter.OPTION_RESPONSE);
	}

	@Override
	public boolean isSupported(URI uri) {
		return true;
	}

	@Override
	public boolean exists(URI uri, Map<?, ?> options) {
		try {
			URL url = new URL(uri.toString());
			URLConnection urlConnection = url.openConnection();

			int timeout = getTimeout(options);
			if (timeout != 0) {
				urlConnection.setConnectTimeout(timeout);
				urlConnection.setReadTimeout(timeout);
			}

			if (urlConnection instanceof HttpURLConnection) {
				HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
				httpURLConnection.setRequestMethod("HEAD");
				int responseCode = httpURLConnection.getResponseCode();
				// Note:
				// Folders will often return 401 or even 403.
				// Consider something to exist even though access is unauthorized or forbidden?
				return responseCode == HttpURLConnection.HTTP_OK;

			} else {
				InputStream inputStream = urlConnection.getInputStream();
				inputStream.close();
				return true;
			}

		} catch (Throwable exception) {
			return false;
		}
	}

	@Override
	public boolean createNewResource(URI uri, Map<Object, Object> options) throws IOException {
		return false;
	}

	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		try {
			URL url = new URL(uri.toString());
			final URLConnection urlConnection = url.openConnection();

			int timeout = getTimeout(options);
			if (timeout != 0) {
				urlConnection.setConnectTimeout(timeout);
				urlConnection.setReadTimeout(timeout);
			}

			InputStream result = urlConnection.getInputStream();
			Map<Object, Object> response = getResponse(options);
			if (response != null) {
				response.put(URIConverter.RESPONSE_TIME_STAMP_PROPERTY, urlConnection.getLastModified());
			}
			return result;

		} catch (RuntimeException exception) {
			throw new IOException(exception);
		}
	}

	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		try {
			URL url = new URL(uri.toString());
			final URLConnection urlConnection = url.openConnection();
			urlConnection.setDoOutput(true);

			int timeout = getTimeout(options);
			if (timeout != 0) {
				urlConnection.setConnectTimeout(timeout);
			}

			if (urlConnection instanceof HttpURLConnection) {
				final HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
				httpURLConnection.setRequestMethod("PUT");

				return new FilterOutputStream(urlConnection.getOutputStream()) {
					@Override
					public void close() throws IOException {
						super.close();
						int responseCode = httpURLConnection.getResponseCode();
						switch (responseCode) {
						case HttpURLConnection.HTTP_OK:
						case HttpURLConnection.HTTP_CREATED:
						case HttpURLConnection.HTTP_NO_CONTENT: {
							break;
						}
						default: {
							throw new IOException("PUT failed with HTTP response code " + responseCode);
						}
						}
					}

					@Override
					public void write(byte[] b, int off, int len) throws IOException {
						out.write(b, off, len);
					}
				};

			} else {
				OutputStream result = urlConnection.getOutputStream();
				final Map<Object, Object> response = getResponse(options);
				if (response != null) {
					result = new FilterOutputStream(result) {
						@Override
						public void close() throws IOException {
							try {
								super.close();
							} finally {
								response.put(URIConverter.RESPONSE_TIME_STAMP_PROPERTY, urlConnection.getLastModified());
							}
						}

						@Override
						public void write(byte[] b, int off, int len) throws IOException {
							out.write(b, off, len);
						}
					};
				}
				return result;
			}
		} catch (RuntimeException exception) {
			throw new IOException(exception);
		}
	}

	@Override
	public boolean delete(URI uri, Map<?, ?> options) throws IOException {
		try {
			URL url = new URL(uri.toString());
			URLConnection urlConnection = url.openConnection();
			urlConnection.setDoOutput(true);

			int timeout = getTimeout(options);
			if (timeout != 0) {
				urlConnection.setConnectTimeout(timeout);
			}

			if (urlConnection instanceof HttpURLConnection) {
				final HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
				httpURLConnection.setRequestMethod("DELETE");
				int responseCode = httpURLConnection.getResponseCode();
				switch (responseCode) {
				case HttpURLConnection.HTTP_OK:
				case HttpURLConnection.HTTP_ACCEPTED:
				case HttpURLConnection.HTTP_NO_CONTENT: {
					break;
				}
				default: {
					throw new IOException("DELETE failed with HTTP response code " + responseCode);
				}
				}
			} else {
				throw new IOException("Delete is not supported for " + uri);
			}
		} catch (RuntimeException exception) {
			throw new IOException(exception);
		}
		return true;
	}

}
