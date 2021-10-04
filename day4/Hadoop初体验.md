```shell
HDFS集群
	hdfs --daemon start namenode|datanode|secondarynamenode
	hdfs --daemon stop  namenode|datanode|secondarynamenode
YARN集群
	yarn --daemon start resourcemanager|nodemanager
	yarn --daemon stop  resourcemanager|nodemanager

HDFS集群
	start-dfs.sh 
	stop-dfs.sh 
YARN集群
	start-yarn.sh
	stop-yarn.sh
Hadoop集群
	start-all.sh
	stop-all.sh 
```

http://master:9870/   Hadoop网页UI  需配置Windows的hosts文件(老版为50070)

http://master:8088/    Yarn 网页UI 

### HDFS初体验    http://master:9870/

![image-20211003192223062](..\day4\image-20211003192223062.png)

```shell
hadoop fs-mkdir /test01
```

![image-20211003192410342](..\day4\image-20211003192410342.png)

```shell
echo 1 >> 1.text   
hadoop fs -put 1.txt /test01  
# 上传速率慢 why？
```

![image-20211003193120091](..\day4\image-20211003193120091.png)

hdfs  的本质是一个文件系统

### MAPREDUCE+YARN 初体验    http://master:8088/

```shell
cd /software/hadoop  
#cd 切换路径后的每一步都可以用ll查看目录下的文件
cd ./share
cd ./hadoop
cd ./mapreduce
ll
```

![image-20211003194058144](..\day4\image-20211003194058144.png)

```shell
hadoop jar hadoop-mapreduce-examples-3.1.4.jar pi 3 5
#计算圆周率
```

![image-20211003194820294](..\day4\image-20211003194820294.png)

![image-20211003194731611](..\day4\image-20211003194731611.png)

```shell
vi word.txt
# 随意键入几个单词
hadoop fs -mkdir -p /test01/wordcount/in
hadoop fs -put word.txt /test01/wordcount/in
hadoop jar hadoop-mapreduce-examples-3.1.4.jar wordcount /test01/wordcount/in /test01/wordcount/out
# 检测目标路径中的文件中的单词数量
```

![image-20211003202036564](..\day4\image-20211003202036564.png)

![image-20211003205543943](..\day4\image-20211003205543943.png)

![image-20211003205507057](..\day4\image-20211003205507057.png)

1. MapReduce本质是程序？

   分布式计算框架。jar包。java程序代码

2. 执行MapReduce的时候，为什么首先请求YARN?

   运行MapReduce工具时，会先连接到Resource Manager(Yarn中的主角色)，跑程序需要内存等分配，yarn管理集群运算资源，所以MapReduce运行前需要向yarn申请

3. MapReduce看上去好像是两个阶段？

   

4. 先Map,再Reduce？

   

5. 处理小数据的时候，MapReduce速度快吗？

   不快，适用于大数据范围

后续回答



## Hadoop HDFS基准测试

```shell
# 测试写入速度
hadoop jar /software/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-3.1.4-tests.jar TestDFSIO -write -nrFiles 10  -fileSize 10MB
# 向HDFS文件系统中写入数据,10个文件,每个文件10MB,文件存放到/benchmarks/TestDFSIO中
#	Throughput：吞吐量、Average IO rate：平均IO率、IO rate std deviation：IO率标准偏差


# 测试读取速度
hadoop jar /software/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-3.1.4-tests.jar TestDFSIO -read -nrFiles 10 -fileSize 10MB
#在HDFS文件系统中读入10个文件,每个文件10M
#	Throughput：吞吐量、Average IO rate：平均IO率、IO rate std deviation：IO率标准偏差

# 清除测试数据
hadoop jar /software/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-3.1.4-tests.jar TestDFSIO -clean

```

![image-20211003215635245](..\day4\image-20211003215635245.png)

![image-20211003220242461](..\day4\image-20211003220242461.png)

![image-20211003220622339](D:\Graduate\HadoopLearning\day4\image-20211003220622339.png)

















