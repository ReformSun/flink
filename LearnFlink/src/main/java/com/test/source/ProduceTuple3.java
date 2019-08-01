package com.test.source;

import com.test.util.TimeUtil;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.typeutils.TupleTypeInfo;

public class ProduceTuple3 implements ProduceData<Tuple3<String,Integer,Long>>{
	private int i = 0;
	private long time = 0;

	public ProduceTuple3() {
		time = TimeUtil.toLong("2019-07-4 1:34:00:000");
	}

	@Override
	public Tuple3<String, Integer, Long> getData() {
		return getData2();
	}

	@Override
	public TypeInformation<Tuple3<String, Integer, Long>> getProducedType() {
		return new TupleTypeInfo(Types.STRING,Types.INT,Types.LONG);
	}

	public Tuple3<String, Integer, Long> getData1() {
		Tuple3<String,Integer,Long> tuple3;
		if (i % 2 == 0){
			tuple3 = new Tuple3<>("a",1,time);
		}else {
			tuple3 = new Tuple3<>("b",1,time);
		}
		i++;
		time = time + 60000;
		System.out.println(tuple3.toString());
		return tuple3;
	}

	public Tuple3<String, Integer, Long> getData2() {
		Tuple3<String,Integer,Long> tuple3;
		tuple3 = new Tuple3<>("a",1,time);
		time = time + 60000;
		System.out.println(tuple3.toString());
		return tuple3;
	}

}
