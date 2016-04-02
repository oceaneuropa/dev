Project osgi.iot.contest.sdk
http://www.programcreek.com/java-api-examples/index.php?source_dir=osgi.iot.contest.sdk-master/osgi.enroute.gogo.extra.provider/src/osgi/enroute/gogo/extra/provider/ExtraImpl.java
https://github.com/osgi/osgi.iot.contest.sdk.git
http://enroute.osgi.org/trains/200-architecture.html

Karaf
http://grepcode.com/file/repo1.maven.org/maven2/org.ow2.chameleon.fuchsia.tools/fuchsia-gogo-shell/0.0.3/org/ow2/chameleon/fuchsia/tools/shell/util/FuchsiaGogoUtil.java#FuchsiaGogoUtil.getArgumentValue%28java.lang.String%2Cjava.lang.String%5B%5D%29
Karaf SSH example
Karaf JMX example
https://karaf.apache.org/manual/latest-3.0.x/developers-guide/connect.html

https://karaf.apache.org/manual/latest/developers-guide/extending.html

REST request url:
GET all machines:      http://127.0.0.1:9090/mgm/machines
GET one machine by id: http://127.0.0.1:9090/mgm/machines/{machineId}


OSGi command:
mgm:login -url http://127.0.0.1:9090 -u admin -p 123

mgm:list -machine

mgm:list -home -machineid b83f1a2c-49b6-4e1e-8f01-183b009d3397
mgm:list -home -machineid c112e832-2c7a-4b04-bc7e-a36f83a6d1a8
mgm:list -home -machineid 09efb046-684f-4659-b913-633aa9d72c83

mgm:list -metasector

mgm:list -metaspace -metasectorid 62f349e2-cf57-4074-9dd7-1e1b05fd1ae7
mgm:list -metaspace -metasectorid 56b182cb-68e3-4d44-aa16-5cef55ae5a40
mgm:list -metaspace -metasectorid 06961ea1-8917-48f5-bd39-251a0760efe1

mgm:create -machine

