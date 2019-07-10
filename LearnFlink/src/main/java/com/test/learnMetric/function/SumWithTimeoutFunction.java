package com.test.learnMetric.function;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class SumWithTimeoutFunction extends KeyedProcessFunction<String,Tuple3<String,Integer,Long>,Tuple3<String,Integer,Long>>{
	private ValueState<SumWithTimestamp> state;
	@Override
	public void processElement(Tuple3<String, Integer, Long> value, Context ctx, Collector<Tuple3<String, Integer, Long>> out) throws Exception {
		SumWithTimestamp current = state.value();
		if (current == null) {
			current = new SumWithTimestamp();
			current.key = value.f0;
			current.sum = value.f1;
		}else {
			current.sum = current.sum + value.f1;
		}

		current.lastModified = value.f2;

		state.update(current);

		ctx.timerService().registerEventTimeTimer(current.lastModified + 60000);
	}

	@Override
	public void open(Configuration parameters) throws Exception {
		state = getRuntimeContext().getState(new ValueStateDescriptor<>("myState", SumWithTimestamp.class));
	}

	@Override
	public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple3<String, Integer, Long>> out) throws Exception {
		SumWithTimestamp result = state.value();

		if (timestamp == result.lastModified + 60000) {
			out.collect(new Tuple3<String,Integer,Long>(result.key, result.sum,timestamp));
		}
	}
}
