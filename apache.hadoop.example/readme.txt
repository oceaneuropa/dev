////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Docs
////////////////////////////////////////////////////////////////////////////////////////////////////////////
1. Hadoop Setup
http://tutorials.techmytalk.com/2014/07/21/apache-hadoop-setup/

2. Hadoop HDFS JAVA API
http://tutorials.techmytalk.com/2014/08/16/hadoop-hdfs-java-api/

3. Hadoop download
http://hadoop.apache.org/releases.html

4. Storing Hierarchical Data in a Database
(1)Storing Hierarchical Data in a Database - The Adjacency List Model
http://www.sitepoint.com/hierarchical-data-database/

(2)Storing Hierarchical Data in a Database, Part 2 - Modified Preorder Tree Traversal
http://www.sitepoint.com/hierarchical-data-database-2/

(3)Storing Hierarchical Data in a Database, Part 3 - 
http://www.sitepoint.com/hierarchical-data-database-3/


5. Hadoop on OSX 鈥淯nable to load realm info from SCDynamicStore鈥�
http://stackoverflow.com/questions/7134723/hadoop-on-osx-unable-to-load-realm-info-from-scdynamicstore

    Add the following to your hadoop-env.sh file:
    export HADOOP_OPTS="-Djava.security.krb5.realm=OX.AC.UK -Djava.security.krb5.kdc=kdc0.ox.ac.uk:kdc1.ox.ac.uk"

    In total, my hadoop-env.sh snippet is as follows:
    HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.realm= -Djava.security.krb5.kdc="
    HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.conf=/dev/null"


6. Hadoop on OSX 鈥淯nable to load realm info from SCDynamicStore鈥�
http://stackoverflow.com/questions/7134723/hadoop-on-osx-unable-to-load-realm-info-from-scdynamicstore
	HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.realm= -Djava.security.krb5.kdc="
	HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.conf=/dev/null"


////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Mac
////////////////////////////////////////////////////////////////////////////////////////////////////////////

1. Create directories
------------------------------------------------------------------------------------------------------------
mkdir -p /Users/yayang/apache/hadoop/hdfs/namenode
mkdir -p /Users/yayang/apache/hadoop/hdfs/datanode
------------------------------------------------------------------------------------------------------------

2. {HADOOP_HOME}/etc/hadoop/yarn-site.xml
------------------------------------------------------------------------------------------------------------
<configuration>
<!-- Site specific YARN configuration properties -->
<property>
   <name>yarn.nodemanager.aux-services</name>
   <value>mapreduce_shuffle</value>
</property>
<property>
   <name>yarn.nodemanager.aux-services.mapreduce.shuffle.class</name>
   <value>org.apache.hadoop.mapred.ShuffleHandler</value>
</property>
</configuration>
------------------------------------------------------------------------------------------------------------

2.{HADOOP_HOME}/etc/hadoop/core-site.xml
------------------------------------------------------------------------------------------------------------
<!-- Put site-specific property overrides in this file. -->
<configuration>
<property>
  <name>fs.defaultFS</name>
  <value>hdfs://localhost:9000/</value>
</property>
</configuration>
------------------------------------------------------------------------------------------------------------ 

3.{HADOOP_HOME}/etc/hadoop/hdfs-site.xml
Mac
------------------------------------------------------------------------------------------------------------
<configuration>
<property>
   <name>dfs.replication</name>
   <value>1</value>
</property>
<property>
   <name>dfs.namenode.name.dir</name>
   <value>file:/Users/yayang/apache/hadoop/hdfs/namenode</value>
</property>
<property>
   <name>dfs.datanode.data.dir</name>
   <value>file:/Users/yayang/apache/hadoop/hdfs/datanode</value>
</property>
</configuration>
------------------------------------------------------------------------------------------------------------

4.{HADOOP_HOME}/etc/hadoop/mapred-site.xml
------------------------------------------------------------------------------------------------------------
<!-- Put site-specific property overrides in this file. -->
<configuration>
<property>
   <name>mapreduce.framework.name</name>
   <value>yarn</value>
</property>
</configuration>
------------------------------------------------------------------------------------------------------------

5. Format namenode
------------------------------------------------------------------------------------------------------------
hadoop namenode -format (deprecated)
hdfs namenode -format

# hadoop datanode -format
------------------------------------------------------------------------------------------------------------

6. Start namenode
------------------------------------------------------------------------------------------------------------
sh hadoop-daemon.sh start namenode
sh hadoop-daemon.sh stop namenode
------------------------------------------------------------------------------------------------------------

7. Start datanode
------------------------------------------------------------------------------------------------------------
sh hadoop-daemon.sh start datanode
sh hadoop-daemon.sh stop datanode
------------------------------------------------------------------------------------------------------------

8. Start Resource Manager
------------------------------------------------------------------------------------------------------------
sh yarn-daemon.sh start resourcemanager
sh yarn-daemon.sh stop resourcemanager
------------------------------------------------------------------------------------------------------------

9. Start Node Manager
------------------------------------------------------------------------------------------------------------
sh yarn-daemon.sh start nodemanager
sh yarn-daemon.sh stop nodemanager
------------------------------------------------------------------------------------------------------------

10. Start Job History Server
------------------------------------------------------------------------------------------------------------
sh mr-jobhistory-daemon.sh start historyserver
sh mr-jobhistory-daemon.sh stop historyserver
------------------------------------------------------------------------------------------------------------

11. Use "jps" command to see running nodes
------------------------------------------------------------------------------------------------------------
$ jps
42940 JobHistoryServer
42835 ResourceManager
42717 NameNode
42765 DataNode
42962 Jps
42876 NodeManager
------------------------------------------------------------------------------------------------------------

12. Web interface
------------------------------------------------------------------------------------------------------------
Browse HDFS and check health using http://localhost:50070 in the browser
------------------------------------------------------------------------------------------------------------

hadoop-env.sh
------------------------------------------------------------------------------------------------------------
# export HADOOP_OPTS="-Djava.security.krb5.realm=OX.AC.UK -Djava.security.krb5.kdc=kdc0.ox.ac.uk:kdc1.ox.ac.uk"
# HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.realm=-Djava.security.krb5.kdc="
# HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.conf=/dev/null"
# export HADOOP_OPTS
# export HADOOP_OPTS="$HADOOP_OPTS -Djava.library.path=/Users/yayang/apache/hadoop/hadoop-2.7.1/lib/"
# export HADOOP_COMMON_LIB_NATIVE_DIR="/Users/yayang/apache/hadoop/hadoop-2.7.1/lib/native"
------------------------------------------------------------------------------------------------------------


////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Windows
////////////////////////////////////////////////////////////////////////////////////////////////////////////

1. {HADOOP_HOME}/etc/hadoop/hdfs-site.xml
------------------------------------------------------------------------------------------------------------
<configuration>
<property>
   <name>dfs.replication</name>
   <value>1</value>
</property>
<property>
   <name>dfs.namenode.name.dir</name>
   <value>file:/C:/hadoop/hdfs/namenode</value>
</property>
<property>
   <name>dfs.datanode.data.dir</name>
   <value>file:/C:/hadoop/hdfs/datanode</value>
</property>
</configuration>
------------------------------------------------------------------------------------------------------------

2. http://stackoverflow.com/questions/18630019/running-apache-hadoop-2-1-0-on-windows
(1) Format namenode
------------------------------------------------------------------------------------------------------------
hdfs namenode –format
------------------------------------------------------------------------------------------------------------

(2) Start HDFS (Namenode and Datanode)
------------------------------------------------------------------------------------------------------------
start-dfs
------------------------------------------------------------------------------------------------------------

(3) Start MapReduce aka YARN (Resource Manager and Node Manager)
------------------------------------------------------------------------------------------------------------
start-yarn
------------------------------------------------------------------------------------------------------------

(4) In addition to other solutions, here (https://github.com/srccodes/hadoop-common-2.2.0-bin/archive/master.zip) is a pre-built copy of winutil.exe. Download it and add to $HADOOP_HOME/bin. It works for me.


3. Other windows issues:
(1) http://stackoverflow.com/questions/30964216/hadoop-on-windows-yarn-fails-to-start-with-java-lang-unsatisfiedlinkerror
15/12/04 00:23:32 FATAL nodemanager.NodeManager: Error starting NodeManager java.lang.UnsatisfiedLinkError: org.apache.hadoop.io.nativeio.NativeIO$Windows.createDirectoryWithMode0(Ljava/lang/String;I)V

(2) http://stackoverflow.com/questions/25232179/hadoop-error-all-data-nodes-are-aborting
You seem to be hitting the open file handles limit of your user. This is a pretty common issue, and can be cleared in most cases by increasing the ulimit values (its mostly 1024 by default, easily exhaustible by multi-out jobs like yours).

(3) Steps to build Hadoop 2.6
http://coderearth.org/building-hadoop-26-on-64-bit-windows-7.html


////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Web UI
////////////////////////////////////////////////////////////////////////////////////////////////////////////
Access Hadoop from web page:
------------------------------------------------------------------------------------------------------------
http://localhost:50070
http://localhost:50070/dfshealth.html#tab-overview
------------------------------------------------------------------------------------------------------------


////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Java Library
////////////////////////////////////////////////////////////////////////////////////////////////////////////
1. Hadoop library
http://mvnrepository.com/artifact/org.apache.hadoop

2. ZooKeeper library
http://mvnrepository.com/artifact/org.apache.zookeeper

3. Apache common library
https://commons.apache.org/proper/commons-logging/download_logging.cgi
https://commons.apache.org/proper/commons-collections/download_collections.cgi
http://mvnrepository.com/artifact/commons-configuration
http://mvnrepository.com/artifact/commons-collections
http://mvnrepository.com/artifact/org.apache.commons
http://mvnrepository.com/artifact/org.apache.commons/commons-lang3
http://mvnrepository.com/artifact/org.slf4j/slf4j-api


////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Java Example
////////////////////////////////////////////////////////////////////////////////////////////////////////////

Example java code:
------------------------------------------------------------------------------------------------------------
https://linuxjunkies.wordpress.com/2011/11/21/a-hdfsclient-for-hadoop-using-the-native-java-api-a-tutorial/
------------------------------------------------------------------------------------------------------------


