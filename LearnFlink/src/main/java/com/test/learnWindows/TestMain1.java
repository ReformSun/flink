package com.test.learnWindows;

import com.test.customAssignTAndW.TimestampExtractor;
import com.test.util.connector.CustomSourceUtil;
import com.test.window.TumblingEventTimeWindows;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import test.SunWordWithCount;

public class TestMain1 {
	static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    public static void main(String[] args) throws Exception {


//		testMethod1();
		testMethod2();
        env.execute("Socket Window WordCount");
    }

	public static void testMethod1() {
		// get input data by connecting to the socket
		DataStream<String> text = env.socketTextStream("localhost", 9000, "\n");
		DataStream<String> text2 = env.socketTextStream("localhost", 9010, "\n");

		text.union(text2);

		// parse the data, group it, window it, and aggregate the counts
		DataStream<SunWordWithCount> windowCounts = text
			.flatMap(new FlatMapFunction<String, SunWordWithCount>() {
				@Override
				public void flatMap(String value, Collector<SunWordWithCount> out) {

					for (String word : value.split("\\s")) {
						out.collect(new SunWordWithCount(word, 1L));
					}
				}
			}).keyBy("word")
			.timeWindow(Time.seconds(6), Time.seconds(2))
			.reduce(new ReduceFunction<SunWordWithCount>() {
				@Override
				public SunWordWithCount reduce(SunWordWithCount a, SunWordWithCount b) {
					return new SunWordWithCount(a.word,a.count + b.count);
				}
			});
		windowCounts.print().setParallelism(1);
	}

	public static void testMethod2(){
    	env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    	TimestampExtractor<Tuple3<String, Integer, Long>> timestampExtractor = new TimestampExtractor(Time.seconds(0),2);

	    DataStreamSource<Tuple3<String, Integer, Long>> dataStreamSource = env.addSource(CustomSourceUtil.getSourceTuple3());
	    dataStreamSource.assignTimestampsAndWatermarks(timestampExtractor).keyBy(new KeySelector<Tuple3<String,Integer,Long>, String>() {
			@Override
			public String getKey(Tuple3<String, Integer, Long> value) throws Exception {
				return value.f0;
			}
		}).window(TumblingEventTimeWindows.of(Time.seconds(60))).sum(1).print();
	}


}
