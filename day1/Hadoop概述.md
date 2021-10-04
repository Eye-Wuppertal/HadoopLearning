## Hadoop概述

### 分布式技术的特点 

![image-20210929131157124](C:\Users\90809\AppData\Roaming\Typora\typora-user-images\image-20210929131157124.png)

分布式：多台机器每台机器上部署不同组件

集群：多台机器每台集群部署相同组件

### Hadoop 简介

狭义是Apache的一款开源软件

Hadoop核心组件
    Hadoop HDFS（分布式文件存储系统）：解决海量数据存储
    Hadoop YARN（集群资源管理和任务调度框架）：解决资源任务调度
    Hadoop MapReduce（分布式计算框架）：解决海量数据计算

广义上Hadoop指的是围绕Hadoop打造的大数据生态圈。

### Hadoop 特性优点

1. 扩容能力 

   Hadoop是在可用的计算机集群间分配数据并完成计算任务的，这些集群可用方便的扩展到数以千计的节点中。

2. 成本低

   Hadoop通过普通廉价的机器组成服务器集群来分发以及处理数据，以至于成本很低

3. 效率高

   通过并发数据，Hadoop可以在节点之间动态并行的移动数据，使得速度非常快

4. 可靠性

   能自动维护数据的多份复制，并且在任务失败后能自动地重新部署（redeploy）计算任务。所以Hadoop的按位存储和处理数据的能力值得人们信赖。

### Hadoop 架构变迁

Hadoop 1.0
	HDFS（分布式文件存储）
	MapReduce（资源管理和分布式数据处理）

Hadoop 2.0
	HDFS（分布式文件存储）
	MapReduce（分布式数据处理）
	YARN（集群资源管理、任务调度）

Hadoop 3.0架构组件和Hadoop 2.0类似,3.0着重于性能优化。

1. 通用
   精简内核、类路径隔离、shell脚本重构
2. Hadoop HDFS
   EC纠删码、多NameNode支持
3. Hadoop MapReduce
   任务本地化优化、内存参数自动推断
4. Hadoop YARN
   Timeline Service V2、队列配置