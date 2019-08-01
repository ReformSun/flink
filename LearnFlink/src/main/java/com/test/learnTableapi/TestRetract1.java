package com.test.learnTableapi;

import com.test.sink.CustomPrintTuple;
import com.test.sink.CustomRowPrint;
import com.test.util.connector.CustomSourceUtil;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.Table;
import org.apache.flink.types.Row;

public class TestRetract1 extends CommonTable{
	public static void main(String[] args) {
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		tableEnvironment.registerTableSource("filesource", CustomSourceUtil.getTableRow());

		testMethod1();
		printStreamGraph();

		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testMethod1(){
		Table sqlResult = tableEnvironment.sqlQuery("select SUM(user_count) FROM filesource");
		DataStream<Tuple2<Boolean,Row>> stream = tableEnvironment.toRetractStream(sqlResult, Row.class);
		stream.addSink(new CustomPrintTuple<>("test.txt"));
	}
}
