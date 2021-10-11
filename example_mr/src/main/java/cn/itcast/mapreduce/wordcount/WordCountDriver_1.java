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

        //配置运行模式 默认为local
        conf.set("mapreduce.framework.name","local");

        //构建Job作业的实例    参数(配置对象， Job名)
        Job job = Job.getInstance(conf, WordCountDriver_1.class.getSimpleName());

        //设置mr程序运行的主类
        job.setJarByClass(WordCountDriver_1.class);
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
