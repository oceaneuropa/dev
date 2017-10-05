Chapter 1. Installation
http://www.jgroups.org/tutorial/html/ch01.html

	Download
	http://sourceforge.net/projects/javagroups/files/JGroups/

Chapter 2. Writing a simple application
http://www.jgroups.org/tutorial/html/ch02.html


Example command 1:
(in the folder which contains the jar)
----------------------------------------------------------------
java -jar jgroups-3.6.13.Final.jar
----------------------------------------------------------------
output:
----------------------------------------------------------------
Version:      3.6.13.Final (Tourmalet)
----------------------------------------------------------------

Example command 2:
(in the folder which contains the jar)
----------------------------------------------------------------
java -cp ./jgroups-3.6.13.Final.jar -Djava.net.preferIPv4Stack=true org.jgroups.demos.Draw
java -cp jgroups-3.6.13.Final.jar -Djava.net.preferIPv4Stack=true org.jgroups.demos.Draw
----------------------------------------------------------------


SimpleChatDemo
VM arguments:
------------------------------------------------------------------------------------------------------------------
-Djava.net.preferIPv4Stack=true -Dcluster.name=group1 -Duser.name=user11 -Djgroups.print_uuids=false
-Djava.net.preferIPv4Stack=true -Dcluster.name=group1 -Duser.name=user12 -Djgroups.print_uuids=false
-Djava.net.preferIPv4Stack=true -Dcluster.name=group1 -Duser.name=user13 -Djgroups.print_uuids=false
------------------------------------------------------------------------------------------------------------------


ReplicatedHashMapDemo
VM arguments:
------------------------------------------------------------------------------------------------------------------
-Djava.net.preferIPv4Stack=true -Dcluster.name=group1 -Duser.name=user11
-Djava.net.preferIPv4Stack=true -Dcluster.name=group1 -Duser.name=user12
-Djava.net.preferIPv4Stack=true -Dcluster.name=group1 -Duser.name=user13
------------------------------------------------------------------------------------------------------------------

EventBusDemo
------------------------------------------------------------------------------------------------------------------
-Djava.net.preferIPv4Stack=true -Dcluster.name=group1 -Duser.name=user11
-Djava.net.preferIPv4Stack=true -Dcluster.name=group1 -Duser.name=user12
-Djava.net.preferIPv4Stack=true -Dcluster.name=group1 -Duser.name=user13
------------------------------------------------------------------------------------------------------------------



