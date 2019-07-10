package com.test.learnMetric.function;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class SumProcessFunction extends KeyedProcessFunction<String,Tuple3<String,Integer,Long>,Tuple3<String,Integer,Long>> {
	private ValueState<SumValue> state;
	@Override
	public void processElement(Tuple3<String, Integer, Long> value, Context ctx, Collector<Tuple3<String, Integer, Long>> out) throws Exception {
		SumValue current = state.value();
		if (current == null) {
			current = new SumValue();
			current.key = value.f0;
			current.sum = value.f1;
		}else {
			current.sum = current.sum + value.f1;
		}
		state.update(current);
		out.collect(new Tuple3<>(current.key,current.sum,value.f2));
	}

	@Override
	public void open(Configuration parameters) throws Exception {
		state = getRuntimeContext().getState(new ValueStateDescriptor<>("myState", SumValue.class));
	}
}
