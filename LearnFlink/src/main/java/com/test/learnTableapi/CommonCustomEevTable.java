package com.test.learnTableapi;

import com.test.env.CustomStreamEnvironment;
import com.test.learnWindows.CommonCustomWindow;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.apache.flink.table.api.java.StreamTableEnvironment;

public abstract class CommonCustomEevTable extends CommonCustomWindow{
	public final static StreamTableEnvironment tableEnvironment;
	static {
		tableEnvironment = StreamTableEnvironment.getTableEnvironment(env);
	}
}
