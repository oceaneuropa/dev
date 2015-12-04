Hadoop Setup
http://tutorials.techmytalk.com/2014/07/21/apache-hadoop-setup/

Hadoop HDFS JAVA API
http://tutorials.techmytalk.com/2014/08/16/hadoop-hdfs-java-api/

Hadoop download
http://hadoop.apache.org/releases.html

Storing Hierarchical Data in a Database - The Adjacency List Model
http://www.sitepoint.com/hierarchical-data-database/

Storing Hierarchical Data in a Database, Part 2 - Modified Preorder Tree Traversal
http://www.sitepoint.com/hierarchical-data-database-2/

Storing Hierarchical Data in a Database, Part 3 - 
http://www.sitepoint.com/hierarchical-data-database-3/


Hadoop on OSX “Unable to load realm info from SCDynamicStore”
http://stackoverflow.com/questions/7134723/hadoop-on-osx-unable-to-load-realm-info-from-scdynamicstore

    Add the following to your hadoop-env.sh file:
    export HADOOP_OPTS="-Djava.security.krb5.realm=OX.AC.UK -Djava.security.krb5.kdc=kdc0.ox.ac.uk:kdc1.ox.ac.uk"

    In total, my hadoop-env.sh snippet is as follows:
    HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.realm= -Djava.security.krb5.kdc="
    HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.conf=/dev/null"


Hadoop on OSX “Unable to load realm info from SCDynamicStore”
http://stackoverflow.com/questions/7134723/hadoop-on-osx-unable-to-load-realm-info-from-scdynamicstore
	HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.realm= -Djava.security.krb5.kdc="
	HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.conf=/dev/null"


hadoop-env.sh
------------------------------------------------------------------------------------------------------------
# export HADOOP_OPTS="-Djava.security.krb5.realm=OX.AC.UK -Djava.security.krb5.kdc=kdc0.ox.ac.uk:kdc1.ox.ac.uk"
# HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.realm=-Djava.security.krb5.kdc="
# HADOOP_OPTS="${HADOOP_OPTS} -Djava.security.krb5.conf=/dev/null"
# export HADOOP_OPTS
# export HADOOP_OPTS="$HADOOP_OPTS -Djava.library.path=/Users/yayang/apache/hadoop/hadoop-2.7.1/lib/"
# export HADOOP_COMMON_LIB_NATIVE_DIR="/Users/yayang/apache/hadoop/hadoop-2.7.1/lib/native"
------------------------------------------------------------------------------------------------------------


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



Access Hadoop from web page:
http://localhost:50070/dfshealth.html#tab-overview

Hadoop library
http://mvnrepository.com/artifact/org.apache.hadoop

ZooKeeper library
http://mvnrepository.com/artifact/org.apache.zookeeper

Apache common library
https://commons.apache.org/proper/commons-logging/download_logging.cgi
https://commons.apache.org/proper/commons-collections/download_collections.cgi
http://mvnrepository.com/artifact/commons-configuration
http://mvnrepository.com/artifact/commons-collections
http://mvnrepository.com/artifact/org.apache.commons
http://mvnrepository.com/artifact/org.apache.commons/commons-lang3
http://mvnrepository.com/artifact/org.slf4j/slf4j-api


Example java code:
------------------------------------------------------------------------------------------------------------
https://linuxjunkies.wordpress.com/2011/11/21/a-hdfsclient-for-hadoop-using-the-native-java-api-a-tutorial/
------------------------------------------------------------------------------------------------------------
