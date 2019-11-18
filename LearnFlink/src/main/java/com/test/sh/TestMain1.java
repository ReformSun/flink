package com.test.sh;

import com.test.source.CustomSource;
import com.test.source.ProduceTuple3;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.io.IOException;

public class TestMain1 {
	public static void main(String[] args) throws IOException {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.enableCheckpointing(6000);
        testMethod1(env);
		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testMethod1(StreamExecutionEnvironment env){
		DataStream<Tuple3<String,Integer,Long>> dataStreamSource1 = env
			.addSource(new CustomSource<>(1000,new ProduceTuple3()))
			.setParallelism(1);
		dataStreamSource1.map(new MapFunction<Tuple3<String,Integer,Long>, Tuple3<String,Integer,Long>>() {
			@Override
			public Tuple3<String, Integer, Long> map(Tuple3<String, Integer, Long> value) throws Exception {
				return value;
			}
		}).addSink(new SinkFunction<Tuple3<String, Integer, Long>>() {
			@Override
			public void invoke(Tuple3<String, Integer, Long> value) throws Exception {
				System.out.println(value.toString());
			}
		});
	}
}
