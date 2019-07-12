package com.test.source;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.sources.*;
import org.apache.flink.table.sources.tsextractors.ExistingField;
import org.apache.flink.table.sources.wmstrategies.BoundedOutOfOrderTimestamps;
import org.apache.flink.table.sources.wmstrategies.WatermarkStrategy;
import org.apache.flink.types.Row;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomTableSourceRow implements
	StreamTableSource<Row>,
	DefinedProctimeAttribute,
	DefinedRowtimeAttributes,
	DefinedFieldMapping {
	private TableSchema schema;
	private String rowTime;
	private long interval = 0;
	private WatermarkStrategy watermarkStrategy;
	private ProduceData<Row> produceData;

	private CustomTableSourceRow(TableSchema schema,String rowTime,long interval,WatermarkStrategy watermarkStrategy,ProduceData<Row> produceData) {
		this.schema = schema;
		this.rowTime = rowTime;
		this.interval = interval;
		this.watermarkStrategy = watermarkStrategy;
		this.produceData = produceData;
	}

	@Nullable
	@Override
	public Map<String, String> getFieldMapping() {
		return null;
	}

	@Nullable
	@Override
	public String getProctimeAttribute() {
		return null;
	}

	@Override
	public List<RowtimeAttributeDescriptor> getRowtimeAttributeDescriptors() {
		RowtimeAttributeDescriptor rowtimeAttributeDescriptor = null;
		List<RowtimeAttributeDescriptor> list = new ArrayList<RowtimeAttributeDescriptor>();
		if (watermarkStrategy == null){
			rowtimeAttributeDescriptor = new RowtimeAttributeDescriptor(rowTime, new ExistingField(rowTime),new BoundedOutOfOrderTimestamps(0L));
		}else {
			rowtimeAttributeDescriptor = new RowtimeAttributeDescriptor(rowTime, new ExistingField(rowTime),watermarkStrategy);
		}
		list.add(rowtimeAttributeDescriptor);
		return list;

	}

	@Override
	public DataStream<Row> getDataStream(StreamExecutionEnvironment execEnv) {
		return execEnv.addSource(new CustomSource<Row>(interval,produceData));
	}

	@Override
	public TypeInformation<Row> getReturnType() {
		return schema.toRowType();
	}

	@Override
	public TableSchema getTableSchema() {
		return schema;
	}

	@Override
	public String explainSource() {
		return "";
	}


	public static CustomTableSourceRow.Builder builder() {
		return new CustomTableSourceRow.Builder();
	}

	public static class Builder{
		private TableSchema schema;
		private String rowTime;
		private long interval = 0;
		private WatermarkStrategy watermarkStrategy;
		private ProduceData<Row> produceData;

		public CustomTableSourceRow.Builder setSchema(TableSchema schema) {
			this.schema = schema;
			return this;
		}

		public CustomTableSourceRow.Builder setProduceData(ProduceData<Row> produceData) {
			this.produceData = produceData;
			return this;
		}

		public CustomTableSourceRow.Builder setRowTime(String rowTime) {
			this.rowTime = rowTime;
			return this;
		}

		public CustomTableSourceRow.Builder setInterval(long interval) {
			this.interval = interval;
			return this;
		}

		public CustomTableSourceRow.Builder setWatermarkStrategy(WatermarkStrategy watermarkStrategy) {
			this.watermarkStrategy = watermarkStrategy;
			return this;
		}

		protected CustomTableSourceRow.Builder builder(){
			return this;
		}

		public CustomTableSourceRow build(){
			return new CustomTableSourceRow(schema,rowTime,interval,watermarkStrategy,produceData);
		}


	}
}
