package com.test.learnWindows;

import test.SunWordWithCount;

public class TestMain1 {
    public static void main(String[] args) throws Exception {
//        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//		testMethod2(env);
//        env.execute("Socket Window WordCount");
    }

//	public static void testMethod1(StreamExecutionEnvironment env) {
//		// get input data by connecting to the socket
//		DataStream<String> text = env.socketTextStream("localhost", 9000, "\n");
//		DataStream<String> text2 = env.socketTextStream("localhost", 9010, "\n");
//
//		text.union(text2);
//
//		// parse the data, group it, window it, and aggregate the counts
//		DataStream<SunWordWithCount> windowCounts = text
//			.flatMap(new FlatMapFunction<String, SunWordWithCount>() {
//				@Override
//				public void flatMap(String value, Collector<SunWordWithCount> out) {
//
//					for (String word : value.split("\\s")) {
//						out.collect(new SunWordWithCount(word, 1L));
//					}
//				}
//			}).keyBy("word")
//			.timeWindow(Time.seconds(6), Time.seconds(2))
//			.reduce(new ReduceFunction<SunWordWithCount>() {
//				@Override
//				public SunWordWithCount reduce(SunWordWithCount a, SunWordWithCount b) {
//					return new SunWordWithCount(a.word,a.count + b.count);
//				}
//			});
//
//
//
//
//		windowCounts.print().setParallelism(1);
//	}
//
//	public static void testMethod2(StreamExecutionEnvironment env) {
//		// get input data by connecting to the socket
//		DataStream<String> text = env.fromElements("aaa","cccc");
//		DataStream<String> text2 = env.fromElements("cccc","aaaaaaa");
//
//		DataStream<String> dataStream = text.union(text2);
//
//		DataStream<SunWordWithCount> windowCounts = dataStream
//			.flatMap(new FlatMapFunction<String, SunWordWithCount>() {
//				@Override
//				public void flatMap(String value, Collector<SunWordWithCount> out) {
//
//					for (String word : value.split("\\s")) {
//						out.collect(new SunWordWithCount(word, 1L));
//					}
//				}
//			});
//
//		DataStream<SunWordWithCount> dataStream1 = windowCounts.keyBy("word")
//			.timeWindow(Time.seconds(6), Time.seconds(2))
//			.reduce(new ReduceFunction<SunWordWithCount>() {
//				@Override
//				public SunWordWithCount reduce(SunWordWithCount a, SunWordWithCount b) {
//					return new SunWordWithCount(a.word,a.count + b.count);
//				}
//			});
//		dataStream1.print().setParallelism(1);
//	}

}
