package jersey.example3;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JsonContentWriter implements MessageBodyWriter<OrderBean> {

	@Override
	public long getSize(OrderBean arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4) {

		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		if (arg0.getName().equals(OrderBean.class.getName()))
			return true;

		return false;
	}

	@Override
	public void writeTo(OrderBean orderBean, Class<?> classInstance, Type type, Annotation[] annotationArray, MediaType mediaType, MultivaluedMap<String, Object> valueMap, OutputStream outputStream) throws IOException, WebApplicationException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(outputStream, orderBean);
	}

}