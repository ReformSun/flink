package com.test.learnTableapi;

import com.test.udf.StringLengthUdf;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.Arrays;
import java.util.List;

public class UDFTCase extends UDCommonTable{
	public static void main(String[] args) {
		testMethod1();
	}

	public static void testMethod1(){
	    tableEnvironment.registerFunction("StringLengthUdf",new StringLengthUdf());
	    String sql = "select StringLengthUdf(Company),OrderNumber from table1";
	    procTimePrint(sql);
	}
}
