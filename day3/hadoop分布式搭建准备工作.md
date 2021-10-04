VM, CentOS7, finalshell

![image-20211002001352799](..\day3\image-20211002001352799.png)

![image-20211002001420137](..\day3\image-20211002001420137.png)

![image-20211002001453866](..\day3\image-20211002001453866.png)

![image-20211002001523705](..\day3\image-20211002001523705.png)

![image-20211002001633450](..\day3\image-20211002001633450.png)

![image-20211002001714914](..\day3\image-20211002001714914.png)

![image-20211002001918188](..\day3\image-20211002001918188.png)

![image-20211002010156748](..\day3\image-20211002010156748.png)

vi /etc/sysconfig/network-scripts/ifcfg-ens33     //克隆节点需要更改ip address

![image-20211002005052307](..\day3\image-20211002005052307.png)

systemctl stop firewalld

克隆2个

 service network restart 重启网卡

拍摄快照备份

若是出现多虚拟机蓝屏重启则需要修改win的虚拟内容分配

vi  /etc/hostname  修改主机名

vi /etc/hosts   修改映射

![image-20211002130902760](..\day3\image-20211002130902760.png)

scp /etc/hosts salve1:etc  将映射文件拷贝到其他两个节点

![image-20211002132229325](..\day3\image-20211002132229325.png)

创建hadoop和jdk安装包下载路径和软件安装路径

![image-20211002133010633](..\day3\image-20211002133010633.png)

![image-20211002133020284](..\day3\image-20211002133020284.png)

tar zxvf /data/packs/jdk...  -C /software/  

tar zxvf /data/packshadoop...  -C /software/    解压hadoop和jdk

![image-20211002133722796](..\day3\image-20211002133722796.png)

重命名

配置环境变量

```shell
vi 	/etc/profile   修改环境变量
# java and hadoop
export JAVA_HOME=/software/java
export HADOOP_HOME=/software/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$JAVA_HOME/bin:$HADOOP_HOME/sbin:$JAVA_HOME/sbin
. /etc/profile   刷新配置文件
```

需要配置的Hadoop文件

![image-20211002135048308](..\day3\image-20211002135048308.png)

```xml
vi core-site.xml
<configuration>
	<!-- 指定namenode的地址 -->
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://master:9000</value>
    </property>
    <!-- 用来指定使用Hadoop时产生文件的存放目录 -->
    <property>
    	<name>hadoop.tmp.dir</name>
        <value>/data/tmp/hadoop</value>
    </property>
</configuration>

vi hdfs-site.xml
<configuration>
	<!-- 指定hdfs保存数据的副本数量 -->
    <property>
        <name>hdfs.replication</name>
        <value>2</value>
    </property>
    <!-- 指定hdfs中namenode的存储位置 -->
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>/data/tmp/hadoop/name</value>
    </property>
    <!-- 指定hdfs中dataname的存储位置 -->
    <property>
    	<name>dfs.datanode.data.dir</name>
        <value>/data/tmp/hadoop/data</value>
    </property>
    <!-- 第二名称节点 -->
    <property>
    	<name>dfs.secondary.http.address</name>
        <value>slave1:50070</value>
    </property>
</configuration>

vi yarn-site.xml
<configuration>
	<!-- namenodeManager获取数据的方式是shuffle -->
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <!-- 资源管理器 -->
    <property>
    	<name>yarn.resourmanager.webapp.address</name>
        <value>master:8088</value>
    </property>
</configuration>

vi mapred-site.xml
<configuration>
	<!-- 告诉hadoop以后MR（mapreduce）运行在YARN上 -->
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>

vi slaves
localhost
master
slave1
slave2

vi hadoop-env.sh
vi yarn-env.sh
import JAVA_HOME=/software/java

```

拷贝配置文件到从节点

```shell
scp /etc/profile slave1:/etc/
scp -r /software/ slave1:/
(刷新从节点的环境变量 ./etc/profile)
```

格式化namenode       

hdfs namenode -format

启动  /software/hadoop/sbin/start-all.sh



黑马

systemctl stop firewalld.service  
systemctl disable firewalld.service  关闭防火墙

 service network restart

ssh-keygen  ssh密钥生成

ssh-copy-id slave1  实行节点间的免密登录



```xml
修改 hadoop-env.sh 

#配置JAVA_HOME
export JAVA_HOME=/software/java
#设置用户以执行对应角色shell命令
export HDFS_NAMENODE_USER=root
export HDFS_DATANODE_USER=root
export HDFS_SECONDARYNAMENODE_USER=root
export YARN_RESOURCEMANAGER_USER=root
export YARN_NODEMANAGER_USER=root 

修改 core-site.xml
<!-- 默认文件系统的名称。通过URI中schema区分不同文件系统。-->
<!-- file:///本地文件系统 hdfs:// hadoop分布式文件系统 gfs://。-->
<!-- hdfs文件系统访问地址：http://nn_host:9000。-->
<property>
    <name>fs.defaultFS</name>
    <value>hdfs://master:9000</value>
</property>
<!-- hadoop本地数据存储目录 format时自动生成 -->
<property>
    <name>hadoop.tmp.dir</name>
    <value>/data/hadoop</value>
</property>
<!-- 在Web UI访问HDFS使用的用户名。-->
<property>
    <name>hadoop.http.staticuser.user</name>
    <value>root</value>
</property>


hdfs-site.xml
<!-- 设定SNN运行主机和端口。-->
<property>
    <name>dfs.namenode.secondary.http-address</name>
    <value>slave1:9868</value>
</property>

mapred-site.xml
<!-- mr程序默认运行方式。yarn集群模式 local本地模式-->
<property>
  <name>mapreduce.framework.name</name>
  <value>yarn</value>
</property>
<!-- hadoop3特有的配置 -->
<!-- MR App Master环境变量。-->
<property>
  <name>yarn.app.mapreduce.am.env</name>
  <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
</property>
<!-- MR MapTask环境变量。-->
<property>
  <name>mapreduce.map.env</name>
  <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
</property>
<!-- MR ReduceTask环境变量。-->
<property>
  <name>mapreduce.reduce.env</name>
  <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
</property>

yarn-site.xml
<!-- yarn集群主角色RM运行机器。-->
<property>
    <name>yarn.resourcemanager.hostname</name>
    <value>master</value>
</property>
<!-- NodeManager上运行的附属服务。需配置成mapreduce_shuffle,才可运行MR程序。-->
<property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
</property>
<!-- 每个容器请求的最小内存资源（以MB为单位）。-->
<property>
  <name>yarn.scheduler.minimum-allocation-mb</name>
  <value>512</value>
</property>
<!-- 每个容器请求的最大内存资源（以MB为单位）。-->
<property>
  <name>yarn.scheduler.maximum-allocation-mb</name>
  <value>2048</value>
</property>
<!-- 容器虚拟内存与物理内存之间的比率。-->
<property>
  <name>yarn.nodemanager.vmem-pmem-ratio</name>
  <value>4</value>
</property>

vi workers
master
slave1
slave2

scp -r /data slave1:/
scp -r /data slave2:/
scp -r /software slave1:/
scp -r /software slave2:/
scp /etc/profile slave1:/etc/

修改Hadoop环境变量
```

小结![image-20211003000437022](..\day3\image-20211003000437022.png)

格式化namenode     初始换创建目录

hdfs namenode -format

启动  start-all.sh