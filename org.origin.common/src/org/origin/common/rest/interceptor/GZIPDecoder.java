package org.origin.common.rest.interceptor;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

/**
 * 
 * @see https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter12/reader_and_writer_interceptors.html
 * 
 *      The ReaderInterceptorContext parameter allows you to view and modify the HTTP headers associated with this invocation. Since interceptors can be used on
 *      both the client and server side, these headers represent either a client response or a server request. In the example, our aroundReadFrom() method uses
 *      the ReaderInterceptorContext to first check to see if the message body is GZIP encoded. If not, it returns with a call to
 *      ReaderInterceptorContext.proceed(). The ReaderInterceptorContext is also used to get and replace the InputStream of the HTTP message body with a
 *      GZipInputStream. The call to ReaderInterceptorContext.proceed() will either invoke the next registered ReaderInterceptor, or if there aren’t any, invoke
 *      the underlying MessageBodyReader.readFrom() method. The value returned by proceed() is whatever was returned by MessageBodyReader.readFrom(). You can
 *      change this value if you want, by returning a different value from your aroundReadFrom() method.
 * 
 *      There’s a lot of other use cases for interceptors that I’m not going to go into detail with. For example, the RESTEasy project uses interceptors to
 *      digitally sign and/or encrypt message bodies into a variety of Internet formats. You could also use a WriterInterceptor to add a JSONP wrapper to your
 *      JSON content. A ReaderInterceptor could augment the unmarshalled Java object with additional data pulled from the request or response. The rest is up to
 *      your imagination.
 * 
 */
@Provider
public class GZIPDecoder implements ReaderInterceptor {

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext ctx) throws IOException {
		String encoding = ctx.getHeaders().getFirst("Content-Encoding");
		if (!"gzip".equalsIgnoreCase(encoding)) {
			return ctx.proceed();
		}
		GZIPInputStream is = new GZIPInputStream(ctx.getInputStream());
		ctx.setInputStream(is);
		return ctx.proceed();
	}

}
