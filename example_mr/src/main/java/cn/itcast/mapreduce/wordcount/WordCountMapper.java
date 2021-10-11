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
