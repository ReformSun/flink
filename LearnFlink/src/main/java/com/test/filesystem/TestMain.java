package com.test.filesystem;

import com.test.filesource.FileSourceTuple3;
import com.test.learnWindows.CommonWindow;
import com.test.sink.CustomPrintTuple;
import com.test.source.CustomSource;
import com.test.source.ProduceRow;
import com.test.source.ProduceTuple2;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;

public class TestMain extends CommonWindow{
	public static void main(String[] args) {

//		env.setParallelism(4);
		env.getConfig().disableSysoutLogging();
		env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
		env.enableCheckpointing(5000);
//		FsStateBackend fsStateBackend = new FsStateBackend(new Path("file:///Users/apple/Desktop/state/checkpointData").toUri(),new Path
//			("file:///Users/apple/Desktop/state/savepointData").toUri());
//        env.setStateBackend(new RocksDBStateBackend(fsStateBackend));
//        testMethod1();
        testMethod2();
		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testMethod1(){
		DataStream<Tuple2<Long,Long>> dataStreamSource1 = env.addSource(new CustomSource<Tuple2<Long,Long>>(1000,new ProduceTuple2())).setParallelism(1);
		dataStreamSource1.keyBy(0).sum(1).addSink(CommonFileSink.createFileSink1());
	}

	public static void testMethod2(){
		TableSchema.Builder tableSchemaBuilder = TableSchema.builder();
		TableSchema tableSchema = tableSchemaBuilder
			.field("user_name", Types.STRING)
			.field("user_count",Types.LONG)
			.field("_sysTime", Types.SQL_TIMESTAMP)
			.build();
		DataStream<Row> dataStreamSource1 = env.addSource(new CustomSource<Row>(1000,new ProduceRow(tableSchema.toRowType()))).setParallelism(1);
		dataStreamSource1.keyBy(0).sum(1).addSink(CommonFileSink.createFileSink());
	}
}
