package org.orbit.infra.runtime.subs;

/*-
 * e.g.
 * User "A" subscribes type "ConfigReg" with id "platform1"
 * User "B" subscribes type "ConfigReg" with id "platform2"
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface SubService {

	void subscribe(String username, String type, String id);

	void unsubscribe(String username, String type, String id);

}
