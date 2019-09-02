package com.test.learnTableapi;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.functions.TemporalTableFunction;

import java.util.ArrayList;
import java.util.List;

public class TemporalTable {
	public static void main(String[] args) {

	}

	/**
	 * 定义一个临时表
	 */
	public static void testMethod1(){
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

		List<Tuple2<String, Long>> ratesHistoryData = new ArrayList<>();
		ratesHistoryData.add(Tuple2.of("US Dollar", 102L));
		ratesHistoryData.add(Tuple2.of("Euro", 114L));
		ratesHistoryData.add(Tuple2.of("Yen", 1L));
		ratesHistoryData.add(Tuple2.of("Euro", 116L));
		ratesHistoryData.add(Tuple2.of("Euro", 119L));

// Create and register an example table using above data set.
// In the real setup, you should replace this with your own table.
		DataStream<Tuple2<String, Long>> ratesHistoryStream = env.fromCollection(ratesHistoryData);
		Table ratesHistory = tEnv.fromDataStream(ratesHistoryStream, "r_currency, r_rate, r_proctime.proctime");

		tEnv.registerTable("RatesHistory", ratesHistory);

// Create and register a temporal table function.
// Define "r_proctime" as the time attribute and "r_currency" as the primary key.
		TemporalTableFunction rates = ratesHistory.createTemporalTableFunction("r_proctime", "r_currency"); // <==== (1)
		tEnv.registerFunction("Rates", rates);
	}
}
