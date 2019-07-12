package com.test.source;

import com.test.util.TimeUtil;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.types.Row;

import java.sql.Timestamp;

public class ProduceRow implements ProduceData<Row>{
	private TypeInformation<Row> typeInformation;
	private long time = 0;

	public ProduceRow(TypeInformation<Row> typeInformation) {
		this.typeInformation = typeInformation;
		time = TimeUtil.toLong("2019-07-4 1:34:00:000");
	}

	@Override
	public Row getData() {
		Row row = new Row(3);
		row.setField(0,"小张");
		row.setField(1,Long.valueOf(1L));
		row.setField(2,new Timestamp(time));
		time = time + 60000;
		return row;
	}

	@Override
	public TypeInformation<Row> getProducedType() {
		return typeInformation;
	}
}
