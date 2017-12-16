package org.origin.common.rest.interceptor;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * 
 * @see https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter12/reader_and_writer_interceptors.html
 * 
 *      A simple example that illustrates these interfaces in action is adding compression to your input and output streams through content encoding. While most
 *      JAX-RS implementations support GZIP encoding, let’s look at how you might add support for it using a ReaderInterceptor and WriterInterceptor:
 * 
 *      The WriterInterceptorContext parameter allows you to view and modify the HTTP headers associated with this invocation. Since interceptors can be used on
 *      both the client and server side, these headers represent either a client request or a server response. In the example, our aroundWriteTo() method uses
 *      the WriterInterceptorContext to get and replace the OutputStream of the HTTP message body with a GZipOutputStream. We also use it to add a
 *      Content-Encoding header. The call to WriterInterceptorContext.proceed() will either invoke the next registered WriterInterceptor, or if there aren’t
 *      any, invoke the underlying MessageBodyWriter.writeTo() method.
 * 
 */
@Provider
public class GZIPEncoder implements WriterInterceptor {

	@Override
	public void aroundWriteTo(WriterInterceptorContext ctx) throws IOException, WebApplicationException {
		GZIPOutputStream os = new GZIPOutputStream(ctx.getOutputStream());
		ctx.getHeaders().putSingle("Content-Encoding", "gzip");
		ctx.setOutputStream(os);
		ctx.proceed();
		return;
	}

}