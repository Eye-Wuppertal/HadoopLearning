## MapReduce基础思想

先分再合，分而治之

将该应用分解成许多小的部分，分配给多台计算机进行处理

先Map阶段进行拆分，把大数据拆分成若干份小数据，多个程序同时并行计算产生中间结果（不能重复，不能遗漏）

后是Reduce聚合阶段，通过程序对并行的结果进行最终的汇总计算，得出最终的结果。

不可拆分的计算任务或相互间有依赖关系的数据无法进行并行计算

![image-20211007173531812](..\day7\image-20211007173531812.png)

![image-20211007213452269](..\day7\image-20211007213452269.png)

## 抽象模型

map: 对一组数据元素进行某种重复式的处理；
reduce: 对Map的中间结果进行某种进一步的结果整理。

map: (k1; v1) → (k2; v2)
reduce: (k2; [v2]) → (k3; v3)
通过以上两个编程接口，可以看出MapReduce处理的数据类型是<key,value>键值对。

## 框架体系

一个完整的MapReduce程序在分布式运行时有三类实例进程：
MRAppMaster：负责整个程序的过程调度及状态协调
MapTask：负责map阶段的整个数据处理流程
ReduceTask：负责reduce阶段的整个数据处理流程

MapReduce编程模型只能包含一个Map阶段和一个Reduce阶段
如果用户的业务逻辑非常复杂，那就只能多个MapReduce程序串行运行。

![image-20211007210921801](..\day7\image-20211007210921801.png)

## 编程规范

用户编写的程序代码分成三个部分：Mapper，Reducer，Driver

用户自定义的Mapper和Reducer都要继承各自的父类。
Mapper中的业务逻辑写在map()方法中；
Reducer的业务逻辑写在reduce()方法中。
整个程序需要一个Driver来进行提交，提交的是一个描述了各种必要信息的job对象。

```java
public class UserMapper extends Mapper {
    @Override
    protected void map(Object key, Object value, Context context) throws IOException, InterruptedException {
        
    }
}
public class UserReducer extends Reducer{
    @Override
    protected void reduce(Object key, Iterable values, Context context) throws IOException, InterruptedException {
        
    }
}
```

##   MapReduce 内部执行流程

除了Map和Reduce过程,内部包含了很多默认组件和默认的行为，如：组件：读取数据组件InputFormat、输出数据组件OutputFormat；行为：排序（key的字典序排序）、分组（reduce阶段key相同的分为一组，一组调用一次reduce处理）

![image-20211007220630389](..\day7\image-20211007220630389.png)

## 序列化

序列化 (Serialization)是将结构化对象转换成字节流以便于进行网络传输或写入持久存储的过程

反序列化（Deserialization）是将字节流转换为一系列结构化对象的过程，重新创建该对象。

![image-20211007220717021](..\day7\image-20211007220717021.png)

### java中的序列化

需要实现java.io.Serializable接口，把对象表示成一个二进制的字节数组，里面包含了对象的数据，对象的类型信息，对象内部的数据的类型信息等等

### Hadoop序列化

没有采用java的序列化机制，而是实现了自己的序列化机制Writable。

通过Writable接口实现的序列化机制，接口提供两个方法write和readFields。
write叫做序列化方法，用于把对象指定的字段写出去；
readFields叫做反序列化方法，用于从字节流中读取字段重构对象；

![image-20211007222044117](..\day7\image-20211007222044117.png)

和java中的Comparable接口合并，提供一个接口WritableComparable

WritableComparable接口可用于用户自定义对象的比较规则

### Hadoop 封装的数据类型

| Hadoop数据类型  | Java数据类型 |
| --------------- | ------------ |
| BooleanWritable | boolean      |
| ByteWritable    | byte         |
| IntWritable     | int          |
| FloatWritable   | float        |
| LongWritable    | long         |
| DoubleWritable  | double       |
| **Text**        | String       |
| MapWritable     | map          |
| ArrayWritable   | array        |
| NullWritable    | null         |

