package com.test.udtf;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.table.api.Types;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

public class ParseUdtf extends TableFunction<Row>{
	public void eval(String str) {
		if (str.equals("error")){
			collector.collect(new Row(2));
			return;
		}
		String[] split = str.split("\\|");
		String first = split[0];
		String second = split[1];
		Row row = new Row(2);
		row.setField(0, first);
		row.setField(1, second);
		collect(row);
	}

	@Override
	public TypeInformation<Row> getResultType() {
		return new RowTypeInfo(Types.STRING(),Types.STRING());
	}

	//	@Override
//// 如果返回值是Row，则必须重载实现getResultType方法，显式地声明返回的字段类型。
//	public DataType getResultType(Object[] arguments, Class[] argTypes) {
//		return DataTypes.createRowType(DataTypes.STRING, DataTypes.LONG, DataTypes.INT);
//	}
}
