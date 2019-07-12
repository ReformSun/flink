package com.test.learnTableapi;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;

public abstract class CommonTable {
	public final static StreamExecutionEnvironment env;
	public final static StreamTableEnvironment tableEnvironment;
	static {
		env = StreamExecutionEnvironment.getExecutionEnvironment();
		tableEnvironment = StreamTableEnvironment.getTableEnvironment(env);
	}
}
