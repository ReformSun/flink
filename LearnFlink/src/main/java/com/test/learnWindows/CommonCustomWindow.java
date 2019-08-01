package com.test.learnWindows;

import com.test.env.CustomStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.apache.flink.table.api.java.StreamTableEnvironment;

public class CommonCustomWindow {
	public final static CustomStreamEnvironment env;
	static {
		env = new CustomStreamEnvironment();
	}
	public static void printStreamGraph(){
		StreamGraph streamGraph = env.getStreamGraph();
		System.out.println("StreamingPlanAsJSON");
		System.out.println(streamGraph.getStreamingPlanAsJSON());
	}
}
