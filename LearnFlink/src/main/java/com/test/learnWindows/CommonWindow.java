package com.test.learnWindows;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.apache.flink.table.api.java.StreamTableEnvironment;

public abstract class CommonWindow {
	public final static StreamExecutionEnvironment env;
	static {
		env = StreamExecutionEnvironment.getExecutionEnvironment();
	}

	public static void printStreamGraph(){
		StreamGraph streamGraph = env.getStreamGraph();
		System.out.println("StreamingPlanAsJSON");
		System.out.println(streamGraph.getStreamingPlanAsJSON());
	}
}
