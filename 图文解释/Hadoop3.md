[TOC]



# 1. 安装虚拟机

VMware

![image-20211011155652898](..\图文解释\image-20211011155652898.png)

![image-20211011155804842](..\图文解释\image-20211011155804842.png)

![image-20211011155857842](..\图文解释\image-20211011155857842.png)

![image-20211011155929723](..\图文解释\image-20211011155929723.png)

![image-20211011160032515](..\图文解释\image-20211011160032515.png)

![image-20211011160138315](..\图文解释\image-20211011160138315.png)

![image-20211011160237915](..\图文解释\image-20211011160237915.png)

一直下一步到完成即可



# 2. 安装Linux系统

使用centOS7
![image-20211011160529108](..\图文解释\image-20211011160529108.png)

![image-20211011160744510](..\图文解释\image-20211011160744510.png)

开启虚拟机，鼠标若是移不出来，可以按住crtl+alt

![image-20211011160945455](..\图文解释\image-20211011160945455.png)

![image-20211011161121352](..\图文解释\image-20211011161121352.png)

![image-20211011161310953](..\图文解释\image-20211011161310953.png)

![image-20211011161349713](..\图文解释\image-20211011161349713.png)

![image-20211011161431159](..\图文解释\image-20211011161431159.png)

![image-20211011161504969](..\图文解释\image-20211011161504969.png)

![image-20211011161748337](..\图文解释\image-20211011161748337.png)

![image-20211011161843387](..\图文解释\image-20211011161843387.png)

![image-20211011161911256](..\图文解释\image-20211011161911256.png)

![image-20211011162030090](..\图文解释\image-20211011162030090.png)

![image-20211011162154993](..\图文解释\image-20211011162154993.png)

这个时候需要查看虚拟机的网络配置

![image-20211011162313074](..\图文解释\image-20211011162313074.png)

![image-20211011162814289](..\图文解释\image-20211011162814289.png)

![image-20211011163010112](..\图文解释\image-20211011163010112.png)

![image-20211011163239120](..\图文解释\image-20211011163239120.png)

点击开始安装

![image-20211011163428913](..\图文解释\image-20211011163428913.png)

重启即可

# 3. 使用FinalShell辅助安装Hadoop和java

## 创建Finalshell与虚拟机的连接

![image-20211011164718180](..\图文解释\image-20211011164718180.png)

![image-20211011165109904](..\图文解释\image-20211011165109904.png)

![image-20211011165145189](..\图文解释\image-20211011165145189.png)

## 基础网络配置

```shell
vi /etc/sysconfig/network-scripts/ifcfg-ens33
```

![image-20211011165753719](..\图文解释\image-20211011165753719.png)

"a"进入编辑，"esc"退出编辑，"shift"+ "zz"退出并保存,后面所有的文件操作都一样

```shell
systemctl stop firewalld.service  
systemctl disable firewalld.service  #关闭防火墙
service network restart  			 #重启网卡
```

```shell
vi /etc/hostname  #修改主机名
```

![image-20211011171333669](..\图文解释\image-20211011171333669.png)

```shell
vi /etc/hosts   #修改映射
```

![image-20211011171838738](..\图文解释\image-20211011171838738.png)

因为总共搭三个节点，故这里提前把其他两个节点的IP地址也写进去了,方便后面直接克隆

## 安装Hadoop和Java

### 创建data目录存储数据，software目录安装软件

```shell
mkdir -p /data/packas
mkdir -p /software
```

### 安装

![image-20211011172551329](..\图文解释\image-20211011172551329.png)

```shell
tar zxvf /data/packas/jdk-8u65-linux-x64.tar.gz -C /software/
tar zxvf /data/packas/hadoop-3.1.4-bin-snappy-CentOS7.tar.gz -C /software/    #解压hadoop和jdk
```

![image-20211011173734795](..\图文解释\image-20211011173734795.png)

### 配置环境变量

```shell
mv /software/jdk1.8.0_65/ /software/java
mv /software/hadoop-3.1.4/ /software/hadoop  #更改软件安装路径名，方便后续操作
```

```shell
vi 	/etc/profile   #修改环境变量
#写在文件末尾即可

# java and hadoop
export JAVA_HOME=/software/java
export HADOOP_HOME=/software/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$JAVA_HOME/bin:$HADOOP_HOME/sbin:$JAVA_HOME/sbin

. /etc/profile   #刷新配置文件
```

![image-20211011174154970](..\图文解释\image-20211011174154970.png)

```shell
java -version
hadoop
#检查环境变量是否出错
```

![image-20211011174348811](..\图文解释\image-20211011174348811.png)

# 4. Hadoop 相关配置

```shell
cd /software/hadoop/etc/hadoop/
# 先打开配置文件所在目录方便后续操作

修改 hadoop-env.sh 
vi hadoop-env.sh 
#配置JAVA_HOME
export JAVA_HOME=/software/java
#设置用户以执行对应角色shell命令
export HDFS_NAMENODE_USER=root
export HDFS_DATANODE_USER=root
export HDFS_SECONDARYNAMENODE_USER=root
export YARN_RESOURCEMANAGER_USER=root
export YARN_NODEMANAGER_USER=root 

```

```xml
修改 core-site.xml   写在configration标签里，后同
vi core-site.xml
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
vi hdfs-site.xml
<!-- 设定SNN运行主机和端口。-->
<property>
    <name>dfs.namenode.secondary.http-address</name>
    <value>slave1:9868</value>
</property>

mapred-site.xml
vi mapred-site.xml
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
vi yarn-site.xml
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

```

```shell
vi workers

master
slave1
slave2
```

# 克隆虚拟机

克隆前需关闭虚拟机,克隆两个

![image-20211011211152853](..\图文解释\image-20211011211152853.png)

![image-20211011211525377](..\图文解释\image-20211011211525377.png)

![image-20211011211619400](E:\Hadoop平台搭建\图文解释\image-20211011211619400.png)