1. test page
http://localhost:8080/mq/readme.html

2. amq ajax example page
http://localhost:8080/mq/amq.html

Note:
example code is from http://activemq.apache.org/ajax.html


3. About org.apache.activemq.brokerURL context parameter
http://stackoverflow.com/questions/2717221/activemq-ajax-client
------------------------------------------------------------------
	<!-- context config -->
    <context-param>
        <param-name>org.apache.activemq.brokerURL</param-name>
        <param-value>tcp://localhost:8686</param-value>
    </context-param>
------------------------------------------------------------------

4. About "A filter or servlet of the current chain does not support asynchronous operations."
http://stackoverflow.com/questions/4104907/servlet-3-0-async-supported-does-not-work
http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html
------------------------------------------------------------------
<async-supported>true</async-supported>
------------------------------------------------------------------

5. http://www.softwareengineeringsolutions.com/blogs/2010/08/13/asynchronous-servlets-in-servlet-spec-3-0/
Note: 
<web-app> needs to have XML declaration, so that <async-supported>true</async-supported> can be added to servlet and filter without validation error.

------------------------------------------------------------------
<web-app xmlns="http://java.sun.com/xml/ns/javaee" 
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
 xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" 
 version="3.0">
------------------------------------------------------------------

6. Deployment Assembly
http://stackoverflow.com/questions/11081411/eclipse-web-app-deployment-with-tomcat-providing-multiple-projects-dependencie

7. Too many consumers on queue after browser restart
http://activemq.2283324.n4.nabble.com/Beginner-problem-Too-many-consumers-on-queue-after-browser-restart-td2719286.html
------------------------------------------------------------------
amq.removeListener(id, destination); 
amq.addListener(id, destination, callBack); 
------------------------------------------------------------------
