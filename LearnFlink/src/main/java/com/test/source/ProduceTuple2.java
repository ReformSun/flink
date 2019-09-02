package com.test.source;

import com.test.util.TimeUtil;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.typeutils.TupleTypeInfo;

public class ProduceTuple2 implements ProduceData<Tuple2<Long,Long>>{
	private long time = 0;

	public ProduceTuple2() {
		time = TimeUtil.toLong("2019-07-4 1:34:00:000");
	}

	@Override
	public Tuple2<Long, Long> getData() {
		return getData1();
	}

	@Override
	public TypeInformation<Tuple2<Long, Long>> getProducedType() {
		return new TupleTypeInfo(Types.LONG,Types.LONG);
	}

	public Tuple2<Long, Long> getData1() {
		Tuple2<Long,Long> tuple2;
		tuple2 = new Tuple2<>(1L,time);
		time = time + 60000;
		System.out.println(tuple2.toString());
		return tuple2;
	}
}
