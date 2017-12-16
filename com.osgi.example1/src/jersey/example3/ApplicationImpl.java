package jersey.example3;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;

/**
 * http://javatech-blog.blogspot.com/2015/04/jax-rs-filters-example.html
 *
 */
@ApplicationPath("rest") // This is similar to binding a URI with container Servlet in web.xml file
public class ApplicationImpl extends javax.ws.rs.core.Application {

	public ApplicationImpl() {
		System.out.println("\n\n===Restful App Initialization===");
	}

	@Override
	public java.util.Set<Object> getSingletons() {
		Set<Object> set = new HashSet<Object>();
		set.add(new JsonContentWriter());
		return set;
	};

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> set = new HashSet<Class<?>>();
		set.add(SelectOrder.class);
		return set;
	}

}
