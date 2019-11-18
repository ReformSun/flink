package com.test.learnTableapi;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.table.api.Table;
import org.apache.flink.types.Row;

import java.util.Arrays;
import java.util.List;

public class UDCommonTable extends CommonTable{
	public static List<Row> table1 = Arrays.asList(
		Row.of( "IBM",3335)
		, Row.of( "理想", 23333)
		, Row.of( "阿里", 3112)
		, Row.of( "华为", 4334)
		, Row.of( "华为", 43334));

	public static void procTimePrint(String sql){
		env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

		DataStreamSource<Row> dataStreamSource_1 = env.fromCollection(table1);

		tableEnvironment.registerDataStream("table1",dataStreamSource_1,"Company,OrderNumber");

		Table table = tableEnvironment.sqlQuery(sql);
		DataStream<Tuple2<Boolean,Row>> dataStream = tableEnvironment.toRetractStream(table,Row.class);
		dataStream.addSink(new SqlOverviewITCase.RetractingSink());
		try {
			env.execute("test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
