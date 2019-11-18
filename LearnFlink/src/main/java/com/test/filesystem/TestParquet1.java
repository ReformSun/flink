package com.test.filesystem;

import com.test.learnWindows.CommonWindow;
import com.test.source.CustomSource;
import com.test.source.ProduceRow;
import com.test.source.ProduceTuple2;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;

import java.io.Serializable;

public class TestParquet1 extends CommonWindow {
	public static void main(String[] args) {

		env.getConfig().disableSysoutLogging();
		env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
		env.enableCheckpointing(5000);
		testMethod1();
		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testMethod1(){
		DataStream<Tuple2<Long,Long>> dataStreamSource1 = env.addSource(new CustomSource<Tuple2<Long,Long>>(1000,new ProduceTuple2())).setParallelism(1);
		DataStream<Datum> dataStream = dataStreamSource1.keyBy(0).sum(1).map(new MapFunction<Tuple2<Long,Long>, Datum>() {
			@Override
			public Datum map(Tuple2<Long, Long> value) throws Exception {
				return new Datum(value.f0,value.f1);
			}
		});
		dataStream.addSink(CommonFileSink.createParquetFileSink());
	}

	public static class Datum implements Serializable {

		public Long a;
		public Long b;

		public Datum() {}

		public Datum(Long a, Long b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			Datum datum = (Datum) o;
			return b == datum.b && (a != null ? a.equals(datum.a) : datum.a == null);
		}

		@Override
		public int hashCode() {
			int result = a != null ? a.hashCode() : 0;
			result = 31 * result + b.hashCode();
			return result;
		}
	}
}
