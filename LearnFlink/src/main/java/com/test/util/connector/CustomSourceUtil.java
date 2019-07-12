package com.test.util.connector;

import com.test.source.*;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.sources.StreamTableSource;
import org.apache.flink.types.Row;

public class CustomSourceUtil {

	public static SourceFunction<Tuple3<String,Integer,Long>> getSourceTuple3(){
		ProduceTuple3 produceTuple3 = new ProduceTuple3();
		return (SourceFunction<Tuple3<String, Integer, Long>>) getSourceTuple3(produceTuple3);
	}
	public static SourceFunction<?> getSourceTuple3(ProduceData<?> produceData){
		return new CustomSource(1000,produceData);
	}

	public static StreamTableSource<Row> getTable(){


		return null;
	}

	/**
	 * 数据生成类
	 * {@link com.test.source.ProduceRow}
	 * @return
	 */
	public static StreamTableSource<Row> getTableRow(){
		CustomTableSourceRow.Builder builder = CustomTableSourceRow.builder();
		TableSchema.Builder tableSchemaBuilder = TableSchema.builder();
		TableSchema tableSchema = tableSchemaBuilder
			.field("user_name", Types.STRING)
			.field("user_count",Types.LONG)
			.field("_sysTime", Types.SQL_TIMESTAMP)
			.build();

		ProduceRow produceRow = new ProduceRow(tableSchema.toRowType());

		CustomTableSourceRow customTableSource = builder.setSchema(tableSchema)
			.setRowTime("_sysTime")
			.setInterval(1000)
			.setWatermarkStrategy(null)
			.setProduceData(produceRow)
			.build();

		return customTableSource;
	}

}
