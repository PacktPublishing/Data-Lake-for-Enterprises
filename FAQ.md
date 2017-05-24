## FAQ's  

This page is aimed at helping readers with some of the common problems that you can face while executing the samples in this book.  

1. What should you do if your HDFS goes into safe mode?  
Execute the below command:  
`hdfs dfsadmin -safemode leave`

2. In hue when navigated to Hive, you see error message “database is locked”. What should you do?  
Execute the below command and then restart the Hue server:  
`${HUE_HOME}/build/env/bin/hue reset_db`  
`${HUE_HOME}/build/env/bin/hue syncdb`  
`${HUE_HOME}/build/env/bin/hue schemamigration desktop --initial`  
`${HUE_HOME}/build/env/bin/hue schemamigration django_extensions --initial`  
`${HUE_HOME}/build/env/bin/hue schemamigration useradmin --initial`  
`${HUE_HOME}/build/env/bin/hue schemamigration beeswax --initial`  
`${HUE_HOME}/build/env/bin/hue migrate desktop`  
`${HUE_HOME}/build/env/bin/hue migrate django_extensions`  
`${HUE_HOME}/build/env/bin/hue migrate useradmin`  
`${HUE_HOME}/build/env/bin/hue migrate beeswax`  

3. If you get missing blocks error (screenshot below) in the Namenode UI (Hadoop browser), what should you do?  
![alt Missing Blocks Error](/common/images/hdfs_missing_blocks_error.png "Missing Blocks Error")
Stop all the processes and execute the below command:  
`hdfs namenode -format`

4. What should you do if namenode is not starting?  
It's not advisable but after repeated tries, if this is the case, to complete the samples in the book, follow the stesps below:  
- Stop all the processes
- Execute the below command:  
`stop-dfs.sh`  
`hdfs namenode -format`

5. What should you do if datanode is not starting?  
	Stop all the processes and clear the tmp folder by executing below command:  
`stop-dfs.sh`  
`rm -rf /tmp/hadoop-*`  

6. What should you do if you want to restart the VM?   
If you want to restart your VM (CentOS), make sure that at least the HDFS (dfs), Zookeeper and Kafka service is stopped using the command (if all the path and related variables set, below should just work). This is required so as to avoid any state inconsistencies due to system restart that may arise in their respective logs and storage. Also make sure that any other processes is killed gracefully so that your VM is not corrupted in anyway.  
`stop-dfs.sh`  
`kafka-server-stop.sh`  
`zookeeper-server-stop.sh`  
`sudo service postgresql stop`  
`stop-yarn.sh`  
`sudo reboot`  

7. To bring back all the various services what should i do?  
It really depends on what all services you really want at the time of running the examples. Below are some of the common services that you can look (for easy one place search).  
After restart of the VM (CentOS), below are the commands which has to be executed to bring all services up:  
Start HDFS - `start-dfs.sh`  
Hue Start - `${HUE_HOME}/build/env/bin/supervisor -d`  
PostgreSQL start - `sudo service postgresql start`  
Zookeeper start (required for Kafka) - `${KAFKA_HOME}/bin/zookeeper-server-start.sh ${KAFKA_HOME}/config/zookeeper.properties`  
Kafka Start - `${KAFKA_HOME}/bin/kafka-server-start.sh ${KAFKA_HOME}/config/server.properties`  
Hive Start - `${HIVE_HOME}/bin/hive --service hiveserver2 -hiveconf hive.root.logging=console`  
Yarn (Hadoop) start - `${HADOOP_HOME}/sbin/start-yarn.sh`  
Elasicsearch start - `${ES_HOME}/bin/elasticsearch`  
Kibana start - `${KIBANA_HOME}/bin/kibana`  

8. How can you figure out what all services are running in the VM?  
Run the commaand `jps` and you should see all the services running (some names doesnt show clear names, but is useful) as shown below.  
![alt JPS command output](/common/images/jps_command_output.png "JPS command output")  

9. What should you do if the IP of your VM (CentOS) changes?  
If due to any reason your CentOS IP changes, get the new IP and run the below command:
`ssh-keyscan -H -t rsa ip_or_ipalias  >> ~/.ssh/known_hosts`  
Note: Change ip_or_ipalias value accordingly

10. How can you check the depth of Kafka topic queues, if you want to see if topic is populated with messages?  
Kafka topic message depth can be displayed by executing the below command:  
`${KAFKA_HOME}/bin/kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list 192.168.0.105:9092 --topic <topic_name>`  
Note: Change topic_name value accordingly  

11. How can you check the messages inside a Kafka topic?  
Execute the below command:  
`${KAFKA_HOME}/bin/kafka-console-consumer.sh --topic <topic_name> --bootstrap-server 192.168.0.102:9092 --from-beginning`  
Note: Change topic_name value accordingly  

12. What are the various URL's where UI's can be seen?  
Various URL’s are as below:  
HDFS - http://ip_of_vm:50070/  
HBase -   
Hue - http://ip_of_vm:8888/   
Flink - http://ip_of_vm:8081  
YARN UI - http://ip_of_vm:8088/cluster  
Elasticsearch - http://ip_of_vm:9200  
Kibana - http://ip_of_vm:5601  
Note: Change ip_of_vm value accordingly  

13. What are the various ports which are getting used?  
Kafka - 9092  
Zookeeper -2181  
PostgreSQL - 5432  

