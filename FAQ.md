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
