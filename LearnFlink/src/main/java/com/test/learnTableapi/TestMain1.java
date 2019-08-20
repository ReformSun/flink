package com.test.learnTableapi;

import com.test.filesource.FileTableSource;
import com.test.sink.CustomRowPrint;
import com.test.sink.CustomRowPrint_Sum;
import com.test.util.connector.CustomSourceUtil;
import com.test.util.connector.FileUtil;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.StreamQueryConfig;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.api.java.Tumble;
import org.apache.flink.types.Row;

public class TestMain1 extends CommonTable{

	public static void main(String[] args) {
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
//		FileTableSource fileTableSource = FileUtil.getFileTableSource2(60000);
		tableEnvironment.registerTableSource("filesource", CustomSourceUtil.getTableRow());

//		testMethod1();
		testMethod2();
		printStreamGraph();

		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void testMethod1(){
		StreamQueryConfig qConfig = new StreamQueryConfig();
		Table sqlResult = tableEnvironment.scan("filesource").where("user_name = '小张'")
			.window(Tumble.over("1.minutes")
				.on("_sysTime").as("w"))
			.groupBy("w")
			.select("user_count.sum,w.end");
//		RowTypeInfo rowTypeInfo = new RowTypeInfo(Types.LONG,Types.STRING);
		DataStream<Row> stream = tableEnvironment.toAppendStream(sqlResult, Row.class);
		stream.addSink(new CustomRowPrint("test.txt",1));
	}

	public static void testMethod2(){
		StreamQueryConfig qConfig = new StreamQueryConfig();
		Table sqlResult = tableEnvironment.sqlQuery("select SUM(user_count),TUMBLE_START(_sysTime, INTERVAL '60' SECOND) " +
			"FROM filesource GROUP BY TUMBLE(_sysTime, INTERVAL '60' SECOND)");
//		RowTypeInfo rowTypeInfo = new RowTypeInfo(Types.LONG,Types.STRING);
		DataStream<Row> stream = tableEnvironment.toAppendStream(sqlResult, Row.class);
		stream.addSink(new CustomRowPrint("test.txt",1));
	}
}
