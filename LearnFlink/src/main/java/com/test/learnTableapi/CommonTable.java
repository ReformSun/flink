package com.test.learnTableapi;

import com.test.learnWindows.CommonWindow;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.apache.flink.table.api.java.StreamTableEnvironment;

public abstract class CommonTable extends CommonWindow{
	public final static StreamTableEnvironment tableEnvironment;
	static {
		tableEnvironment = StreamTableEnvironment.getTableEnvironment(env);
	}
}
