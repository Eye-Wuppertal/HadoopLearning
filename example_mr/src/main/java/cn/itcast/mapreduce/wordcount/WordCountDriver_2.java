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

