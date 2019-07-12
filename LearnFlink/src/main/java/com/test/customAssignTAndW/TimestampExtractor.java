package com.test.customAssignTAndW;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class TimestampExtractor<T extends Tuple> extends BoundedOutOfOrdernessTimestampExtractor<T>{
	private int time_index;

	public TimestampExtractor(Time maxOutOfOrderness, int time_index) {
		super(maxOutOfOrderness);
		this.time_index = time_index;
	}

	@Override
	public long extractTimestamp(T element) {
		return element.getField(time_index);
	}
}
