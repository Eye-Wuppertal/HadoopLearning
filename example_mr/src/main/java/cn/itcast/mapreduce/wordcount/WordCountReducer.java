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
    *   3. 分组之后，同一组数据组成一个新的kv键值对，调用一次方法         recduc方法基于分组调用，一个分组调用一次
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
