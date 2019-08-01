package com.test.learnWindows;

import com.test.customAssignTAndW.TimestampExtractor;
import com.test.sink.CustomPrintTuple;
import com.test.util.connector.CustomSourceUtil;
import com.test.window.SlidingEventTimeWindows;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.windowing.time.Time;

public class TestHopWindow1 extends CommonWindow{
	public static void main(String[] args) {
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		env.getConfig().setAutoWatermarkInterval(100);
		try{
			testMethod1();
			printStreamGraph();
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			env.execute("TestWindow1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testMethod1(){
		DataStreamSource<Tuple3<String,Integer,Long>> dataStreamSourced = env.addSource(CustomSourceUtil.getSourceTuple3()).setParallelism(1);
		dataStreamSourced.assignTimestampsAndWatermarks(new TimestampExtractor<>(Time.seconds(0),2))
			.keyBy(new KeySelector<Tuple3<String,Integer,Long>, String>() {
				@Override
				public String getKey(Tuple3<String, Integer, Long> value) throws Exception {
					return value.f0;
				}
			}).window(SlidingEventTimeWindows.of(Time.seconds(60),Time.seconds(10)))
			.sum(1)
			.addSink(new CustomPrintTuple<>("test.txt"));
	}
}
