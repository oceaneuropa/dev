VM Arguments:
-Declipse.ignoreApp=true -Dosgi.noShutdown=true -DORIGIN_HOME=/Users/example/origin -Dorg.osgi.service.http.port=9090


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
Login
------------------------------------------------------------------------------------------------------
mgm:login -url http://127.0.0.1:9090 -u admin -p 123
------------------------------------------------------------------------------------------------------

Machine
------------------------------------------------------------------------------------------------------
mgm:list -machine
mgm:create -machine -name mymachine1 -ip 192.168.0.1 -description 'this is my machine1'
mgm:create -machine -name mymachine2 -ip 192.168.0.2 -description 'this is my machine2'
mgm:create -machine -name mymachine3 -ip 192.168.0.3 -description 'this is my machine3'
mgm:update -machine b83f1a2c-49b6-4e1e-8f01-183b009d3397 -name m0_1 -ip 127.0.0.9 -description machine0_1
mgm:update -machine b83f1a2c-49b6-4e1e-8f01-183b009d3397 -name m0_2
mgm:update -machine b83f1a2c-49b6-4e1e-8f01-183b009d3397 -ip 127.0.0.10
mgm:update -machine b83f1a2c-49b6-4e1e-8f01-183b009d3397 -description machine0_2
mgm:update -machine b83f1a2c-49b6-4e1e-8f01-183b009d3397 -ip 127.0.0.12 -description machine0_2C
mgm:delete -machine 1acf4c9b-b977-427f-b60b-367ef7be643f
mgm:delete -machine 580cc8a8-b8e6-48e4-8fe1-2f5d5a606439,abca6d6f-5d3d-4967-96ab-7d7c60b7ab15
------------------------------------------------------------------------------------------------------

Home
------------------------------------------------------------------------------------------------------
mgm:list -home -machineid b83f1a2c-49b6-4e1e-8f01-183b009d3397
mgm:list -home -machineid c112e832-2c7a-4b04-bc7e-a36f83a6d1a8
mgm:list -home -machineid 09efb046-684f-4659-b913-633aa9d72c83
mgm:create -home -machineid b83f1a2c-49b6-4e1e-8f01-183b009d3397 -name myhome01 -url C:/clusterM01 -description 'this is my machine01'
mgm:create -home -machineid b83f1a2c-49b6-4e1e-8f01-183b009d3397 -name myhome02 -url C:/clusterM02 -description 'this is my machine02'
mgm:create -home -machineid b83f1a2c-49b6-4e1e-8f01-183b009d3397 -name myhome03 -url C:/clusterM03 -description 'this is my machine03'
mgm:update -home 1e4d969a-66c9-490b-b1be-4412ee8d4206 -name theHome03A -url C:/clusterT03A -description 'this is the machine03A'
mgm:update -home 1e4d969a-66c9-490b-b1be-4412ee8d4206 -name theHome03B
mgm:update -home 1e4d969a-66c9-490b-b1be-4412ee8d4206 -url C:/clusterT03B
mgm:update -home 1e4d969a-66c9-490b-b1be-4412ee8d4206 -description 'this is the machine03B'
mgm:update -home 1e4d969a-66c9-490b-b1be-4412ee8d4206 -url C:/clusterT03C -description 'this is the machine03C'
mgm:delete -home e12457a0-c8c8-4c2a-b12c-e73f10f48891,5ed5c78e-5d07-49e8-b773-ddaa12c23cc5,11b3a48c-3af6-4927-a88a-ea575fe7a3d5
------------------------------------------------------------------------------------------------------

MetaSector
------------------------------------------------------------------------------------------------------
mgm:list -metasector
mgm:create -metasector -name QA -description 'QA meta sector'
mgm:create -metasector -name Dev -description 'Dev meta sector'
mgm:create -metasector -name Production -description 'Production meta sector'

mgm:delete -metasector e0e4ef36-89e3-4a68-8700-f6b914c6b5fc
mgm:delete -metasector 67ad23fe-f3c9-4f0e-8406-8ca44be455dc,7db55c4a-6db6-4cbb-8786-1390be983d83
------------------------------------------------------------------------------------------------------

MetaSpace
------------------------------------------------------------------------------------------------------
mgm:list -metaspace -metasectorid 62f349e2-cf57-4074-9dd7-1e1b05fd1ae7
mgm:list -metaspace -metasectorid 56b182cb-68e3-4d44-aa16-5cef55ae5a40
mgm:list -metaspace -metasectorid 06961ea1-8917-48f5-bd39-251a0760efe1
mgm:create -metaspace -metasectorid 62f349e2-cf57-4074-9dd7-1e1b05fd1ae7 -name myspace1 -description 'my meta space 1'
mgm:create -metaspace -metasectorid 62f349e2-cf57-4074-9dd7-1e1b05fd1ae7 -name myspace2 -description 'my meta space 2'
mgm:create -metaspace -metasectorid 62f349e2-cf57-4074-9dd7-1e1b05fd1ae7 -name myspace3 -description 'my meta space 3'

mgm:delete -metaspace c730ec2c-2b5e-4f18-baa4-ad1e8797e487
mgm:delete -metaspace 05b8cbdc-4c32-4f51-a91a-42062802d920,adbe132f-9183-41cf-98d0-adb488c7bcf9
------------------------------------------------------------------------------------------------------






Login (ManagementLoginCommand)
------------------------------------------------------------------------------------------------------
login -url http://127.0.0.1:9090
------------------------------------------------------------------------------------------------------

Machine (MachineCommand)
------------------------------------------------------------------------------------------------------
lmachines
// show all machines

addmachine -name Machine10 -ip 127.0.0.10 -desc 'description of Machine10'
New Machine is added. 

updatemachine -machineid 0298f2f3-9319-4974-a253-afba47789d23 -name Machine10New -ip 127.0.0.11 -desc 'description of Machine10New'
Machine is updated. 

removemachine -machineid 0298f2f3-9319-4974-a253-afba47789d23
Machine is removed.
------------------------------------------------------------------------------------------------------

Home (HomeCommand)
------------------------------------------------------------------------------------------------------
lhomes
// show all homes

addhome -machineid c1a9977d-4e78-4758-b889-a532afeefb04 -name Home901 -desc 'Description of Home901'
New Home is added.

updatehome -homeid a7b96aba-c93b-4b23-b534-f5044870b77e -name Home901New -desc 'Description of Home901New'
Home is updated.

lhomeproperties
// show home properties

sethomeproperty -homeid a7b96aba-c93b-4b23-b534-f5044870b77e -pname port -pvalue 9091 -ptype string
Home property is set.

sethomeproperty -homeid a7b96aba-c93b-4b23-b534-f5044870b77e -pname url -pvalue http://127.0.0.1:9090 -ptype string
Home property is set.

removehomeproperty -homeid a7b96aba-c93b-4b23-b534-f5044870b77e -pname p1
Home property is removed.

removehome -homeid a7b96aba-c93b-4b23-b534-f5044870b77e
Home is removed.
------------------------------------------------------------------------------------------------------

Project (ProjectCommand)
------------------------------------------------------------------------------------------------------
lprojects
// show projects

createproject -projectid com.eclipse.project5 -name 'Eclipse Project 5' -desc 'Another test project 5'
New Project is created.

updateproject -projectid com.eclipse.project5 -name 'Eclipse Project 5New' -desc 'Another test project 5New'
Project is updated.

deleteproject -projectid com.eclipse.project5
Project is deleted.
------------------------------------------------------------------------------------------------------

Project Home (ProjectHomeCommand)
------------------------------------------------------------------------------------------------------
lprojecthomes
// show homes under projects

createprojecthome -projectid project5 -name 'HomeConfig1' -desc 'Description of Home Config 1'
createprojecthome -projectid project5 -name 'HomeConfig2' -desc 'Description of Home Config 2'
New ProjectHome is created.

updateprojecthome -projecthomeid 904064cc-6bb3-4959-b292-e1f98ac45534 -name HomeConfig1New -desc 'Description of Home Config 1New'
updateprojecthome -projecthomeid 9c99b7be-3c56-42f1-b3a2-a0abff384877 -name HomeConfig2New -desc 'Description of Home Config 2New'
ProjectHome is updated.

deleteprojecthome -projecthomeid 648280a1-e303-4f5d-a452-5a98efa8a2c5
ProjectHome is deleted.

setdeploymenthome -projecthomeid 904064cc-6bb3-4959-b292-e1f98ac45534 -homeid 545cac85-87c9-4921-9f74-9e544baee55a
setdeploymenthome -projecthomeid 9c99b7be-3c56-42f1-b3a2-a0abff384877 -homeid 882a4e4c-65b9-4da3-a1e6-7d658f729bb7
Deployment Home is set.

removedeploymenthome -projecthomeid 904064cc-6bb3-4959-b292-e1f98ac45534 -homeid 545cac85-87c9-4921-9f74-9e544baee55a
removedeploymenthome -projecthomeid 9c99b7be-3c56-42f1-b3a2-a0abff384877 -homeid 882a4e4c-65b9-4da3-a1e6-7d658f729bb7
Deployment Home is removed.
------------------------------------------------------------------------------------------------------