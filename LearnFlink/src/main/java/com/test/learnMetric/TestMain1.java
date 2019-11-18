package com.test.learnMetric;

import com.test.env.CustomStreamEnvironment;
import com.test.filesource.FileSourceTuple3;
import com.test.learnMetric.function.TestRichFlatMapFunction;
import com.test.sink.CustomPrint;
import com.test.sink.CustomPrintTuple3;
import com.test.util.DataUtil;
import com.test.util.RandomUtil;
import com.test.util.StreamExecutionEnvUtil;
import com.test.util.connector.KafkaUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.List;

public class TestMain1 {
	private static final CustomStreamEnvironment env;
	private static int index = 9;
	static {
		env = StreamExecutionEnvUtil.getCustomStreamEnvironment(null);
	}
	public static void main(String[] args) {
		try{
//			testMethod1();
			testMethod2();
//			testMethod3();
			StreamGraph streamGraph = env.getStreamGraph();
			System.out.println(streamGraph.getStreamingPlanAsJSON());
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			env.execute("job_name_test" + index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testMethod1(){
		List<Tuple3<String,Integer,Long>> list = DataUtil.getTuple3_Int_timetamp(5,6,18);
		DataStreamSource<Tuple3<String,Integer,Long>> dataStreamSource = env.fromCollection(list);
		dataStreamSource.map(new MapFunction<Tuple3<String,Integer,Long>, String>() {
			@Override
			public String map(Tuple3<String, Integer, Long> value) throws Exception {
				return value.toString();
			}
		}).addSink(new CustomPrint(null)).name("sink").setParallelism(2);
	}

	public static void testMethod2(){
		env.enableCheckpointing(1000);
		FlinkKafkaConsumer010 kafkaConsumer010 = KafkaUtil.getKafkaConsumer010Source("123abcd");
		kafkaConsumer010.setCommitOffsetsOnCheckpoints(true);

		DataStreamSource<String> dataStreamSource = env.addSource(kafkaConsumer010,"kafkaComsumer" + index);
		dataStreamSource.addSink(new CustomPrint(null)).name("sink" + index);
	}

	public static void testMethod3(){
		DataStreamSource<Tuple3<String,Integer,Long>> dataStreamSource = env.addSource(new FileSourceTuple3(RandomUtil.getRandom(10,1000)),"sourceName" + index).setParallelism
			(1);
		dataStreamSource.flatMap(new TestRichFlatMapFunction()).name("flatmap" + index).addSink(new CustomPrint(null)).name("sink" + index).setParallelism(1);
	}

}
