[TOC]



## 编程思路

map阶段的核心：把输入的数据经过切割，全部标记1。因此输出就是<单词，1>。
shuffle阶段核心：经过默认的排序分区分组，key相同的单词会作为一组数据构成新的kv对。
reduce阶段核心：处理shuffle完的一组数据，该组数据就是该单词所有的键值对。对所有的1进行累加求和，就是单词的总次数。

## 新建工程

![image-20211007223505028](..\day8\image-20211007223505028.png)

```xml
<!-- 配置 porn.xml 添加依赖项 -->
    <dependencies>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>3.1.4</version>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-hdfs</artifactId>
            <version>3.1.4</version>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-client</artifactId>
            <version>3.1.4</version>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-mapreduce-client-core</artifactId>
            <version>3.1.4</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.32</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>2.4</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                            <mainClass></mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.0</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
        </plugins>
    </build>
<!-- 刷新maven依赖项 -->
```

复制log4j.properties到resource目录下

![image-20211008095459663](..\day8\image-20211008095459663.png)

## 新建Mapper类

```java
package cn.itcast.mapreduce.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/*
* KEYIN：表示map阶段输入kv中的key类型      默认情况下是  LongWritable
* VALUEIN：表示map阶段输入kv中的value类型              Text
*       todo mapReduce 有默认的读取数据组件  TextInputFormat
*       todo 读数据的行为是：一行一行读取数据   返回kv键值对
*           K:每一行的起始位置偏移量
*           V:这一行的文本内容
*  KEYOUT：表示map阶段输出kv中的key类型                Text
* VALUEOUT：                 value    与业务相关      LongWritable
*
*/

public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    private Text outkey = new Text();
    private final static LongWritable outvalue = new LongWritable(1);

    /*
    *   map方法是核心
    * 默认情况下，map基于行处理数据
    *
    * */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
       // 将一行数据转换为String
        String line = value.toString();
        // 根据分隔符进行切割
        String [] words = line.split("\\s+");
        // 遍历数组
        for (String word :words){
            outkey.set(word);
            //输出数据 把每个单词标记1 输出<单词, 1>
            context.write(outkey, outvalue);
        }
    }
}
```

## 新建Reducer类

```java
package cn.itcast.mapreduce.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/*
* KEYIN:对应map阶段的KEYOUT          Text
* VALUEIN:         VALUEOUT        LongWritable
* KEYOUT           与业务相关    此处为单词Text
* VALUEOUT                     单词总次数LongWritable
*
* */

public class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
    private LongWritable outvalue = new LongWritable();

    /*
    * todo Q: map的所有输出数据来到reduce后怎么处理
    *   1. 排序   规则：根据key的字典序进行排序 a-z
    *   2. 分组   规则：key相同的分为一组
    *   3. 分组之后，同一组数据组成一个新的kv键值对，调用一次方法    recduc方法基于分组调用，一个分组调用一次
    *       新key    该组共同的key
    *       新value  改组所有value组成的迭代器Iterable
    * 例：      <hello, 1><hadoop, 1><hello, 1><hello, 1><hadoop, 1>
    *      1.  <hadoop, 1><hadoop, 1><hello, 1><hello, 1><hello, 1>
    *      2.  <hadoop, 1><hadoop, 1>
    *          <hello, 1><hello, 1><hello, 1>
    *      3.  <hadoop, 1><hadoop, 1>----><hadoop, Iterable[1,1]>
    *          <hello, 1><hello, 1><hello, 1>------><hello, Iterable[1,1,1]>
    * */
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        //统计变量
        long count = 0;
        //便利该组的values
        for(LongWritable value:values) {
            //累加计算总数
            count += value.get();
        }
        outvalue.set(count);
        //使用上下文对象输出结果
        context.write(key,outvalue);
    }
}
```

## 新建Driver类

### 方法一

```java
package cn.itcast.mapreduce.wordcount;

/*
*   构造Job对象实例
*   制定各种属性，包括mapper reducer类、输入输出文件路径等
*   提交作业    job.submit()
*
* */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountDriver_1 {
    public static void main(String[] args) throws Exception{
        //创建配置对象
        Configuration conf = new Configuration();

        //构建Job作业的实例    参数(配置对象， Job名)
        Job job = Job.getInstance(conf, WordCountDriver.class.getSimpleName());

        //设置mr程序运行的主类
        job.setJarByClass(WordCountDriver.class);
        //设置本次mr程序的mapper类 reducer类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        //指定mapper、reducer阶段输出的key value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //配置本次作业的输入数据路径、输出路径
        //todo 默认组件 TextInputFormat TextOutputFormat
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //最终提交本次job
        //job.submit();
        //waitForCompletion提交job，参数表示是否开启实时监视最终job执行情况
        boolean resultflag = job.waitForCompletion(true);
        //退出程序
        System.exit(resultflag ? 0:1);
    }
}

```

### 方法二

```java
package cn.itcast.mapreduce.wordcount;


/*
* 使用工具类ToolRunner提交MapReduce作业
*
*
* */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCountDriver_2 extends Configured implements Tool {

    public static void main(String[] args) throws Exception{
        //创建配置对象
        Configuration conf = new Configuration();
        //todo 使用工具类ToolRunner提交程序
        int status = ToolRunner.run(conf, new WordCountDriver_2(), args);
        System.exit(status);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(getConf(), WordCountDriver_2.class.getSimpleName());

        //设置mr程序运行的主类
        job.setJarByClass(WordCountDriver_2.class);
        //设置本次mr程序的mapper类 reducer类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        //指定mapper、reducer阶段输出的key value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //配置本次作业的输入数据路径、输出路径
        //todo 默认组件 TextInputFormat TextOutputFormat
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true)? 0: 1;
    }
}


```

## MR程序运行模式

运行在何种模式 取决于参数：mapreduce.framework.name

yarn: YARN集群模式（mapreduce.framework.name=yarn）

local: 本地模式（默认模式）

默认模式在mapred-default.xml中定义。
如果代码中（conf.set）、运行的环境中有配置（mapred-site.xml），会默认覆盖default配置。

### YARN模式

需要配置参数：
mapreduce.framework.name=yarn
yarn.resourcemanager.hostname=node1.itcast.cn

maven主类名补充完整，类名需要全路径

![image-20211008200153635](..\day8\image-20211008200153635.png)

![image-20211008200614644](..\day8\image-20211008200614644.png)

打包 

![image-20211008200955665](..\day8\image-20211008200955665.png)

打包后的jar包直接拖到Linux虚拟机中![image-20211008201456033](..\day8\image-20211008201456033.png)

```shell
hadoop jar example_mr-1.0.jar /data/wordcount/input /data/wordcount/output
```



### Local模式

安装hadoop，并配置环境变量，需将Hadoop.dll添加到Windows/System32目录下

配置运行环境

![image-20211009092045657](..\day8\image-20211009092045657.png)

通常可以在local模式下运行测试，之后在打包成jar包到分布式系统上运行
