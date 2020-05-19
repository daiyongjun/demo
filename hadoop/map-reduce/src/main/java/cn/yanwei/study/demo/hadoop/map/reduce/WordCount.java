package cn.yanwei.study.demo.hadoop.map.reduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 单词统计类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/5/8 11:41
 */
public class WordCount {
    /**
     * 创建Mapper类
     * (input) <KEYIN, VALUEIN> -> map -> <KEYOUT, VALUEOUT>
     * <p>
     * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
     * KEYIN 是指框架读取到的数据的key的类型,默认的InputFormat下，读到的key是一行文本的起始偏移量，所以key的类型是Long，map通过偏移量进行拆分源数据
     * VALUEIN 是指框架读取到的数据的value的类型,在默认的InputFormat下，读到的value是一行文本的内容，所以value的类型是String
     * KEYOUT 是指用户自定义逻辑方法返回的数据中key的类型，由用户业务逻辑决定，在此wordcount程序中，我们输出的key是单词，所以是String
     * VALUEOUT 是指用户自定义逻辑方法返回的数据中value的类型，由用户业务逻辑决定,在此wordcount程序中，我们输出的value是单词的数量，所以是Integer
     * <p>
     * <p>
     * <p>
     * String Long等JDK中自带的数据类型，在序列化时，效率比较低，hadoop为了提高序列化效率，自定义了一套序列化框架,分别LongWritable和Text
     * 在hadoop的程序中，如果该数据需要进行序列化（写磁盘，或者网络传输），就一定要用实现了hadoop序列化框架的数据类型
     * <p>
     * Long ----> LongWritable
     * String ----> Text
     * Integer ----> IntWritable
     * Null ----> NullWritable
     **/
    public static class TokenizerMapper
            extends Mapper<LongWritable, Text, Text, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //处理读取的(input) <KEYIN, VALUEIN>
            //自定义逻辑：对读到的value是一行文本的内容，进行分割
            String[] words = value.toString().split(" ");
            for (String word : words) {
                //构建一个context的类似map -> <KEYOUT, VALUEOUT>
                context.write(new Text(word), new IntWritable(1));
            }
        }

    }

    //combine (input) <k1, v1> -> map -> <k2, v2> -> combine(先将自己收到的所有的kv对按照k分组（根据k是否相同） 将某一组kv中的第一个kv中的k传给reduce方法的key变量，把这一组kv中所有的v用一个迭代器传给reduce方法的变量values) -> <k2, v2> -> reduce -> <k3, v3> (output)


    /**
     * 创建Reducer类
     * <KEYIN, VALUEIN> -> reduce -> <KEYOUT, VALUEOUT> (output)
     * <p>
     * Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
     * <p>
     * KEYIN Reducer类也有输入和输出，输入就是Map阶段的处理结果，输出就是Reduce最后的输出 Text，
     * reducetask在调我们写的reduce方法,reducetask应该收到了前一阶段（map阶段）中所有maptask输出的数据中的一部分，Reducer类也有输入和输出，输入就是Map阶段的处理结果，输出就是Reduce最后的输出 Text，
     * （数据的key.hashcode%reducetask数==本reductask号），所以reducetaks的输入类型必须和maptask的输出类型一样
     * VALUEIN Reducer类也有输入和输出，输入就是Map阶段的处理结果，输出就是Reduce最后的输出 IntWritable
     * KEYOUT  根据 wordcount的返回结果得出 Text类型
     * VALUEOUT 根据 wordcount的返回结果得出IntWritable类型
     * <p>
     * <p>
     * <p>
     * 注意到Iterable<IntWritable> values类型
     * reducetask将这些收到kv数据拿来处理时，是这样调用我们的reduce方法的：
     * 先将自己收到的所有的kv对按照k分组（根据k是否相同）
     * 将某一组kv中的第一个kv中的k传给reduce方法的key变量，把这一组kv中所有的v用一个迭代器传给reduce方法的变量values
     **/
    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            //处理迭代器传给reduce方法的变量values值
            for (IntWritable v : values) {
                sum += v.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}