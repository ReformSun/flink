package com.test.source;

import com.test.filesource.FileSourceBase;
import com.test.filesource.FileTableSource;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
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

public class CustomTableSource<T> implements
	StreamTableSource<T>{
	private TypeInformation<T> tTypeInformation;
	private String rowTime;
	private long interval = 0;
	private WatermarkStrategy watermarkStrategy;
	private ProduceData produceData;

	private CustomTableSource(TypeInformation<T> tTypeInformation, String rowTime, long interval, WatermarkStrategy watermarkStrategy, ProduceData produceData) {
		this.tTypeInformation = tTypeInformation;
		this.rowTime = rowTime;
		this.interval = interval;
		this.watermarkStrategy = watermarkStrategy;
		this.produceData = produceData;
	}

	@Override
	public DataStream<T> getDataStream(StreamExecutionEnvironment execEnv) {
		return execEnv.addSource(new CustomSource<T>(interval,produceData));
	}

	@Override
	public TypeInformation<T> getReturnType() {
		return tTypeInformation;
	}

	@Override
	public TableSchema getTableSchema() {
		return null;
	}

	@Override
	public String explainSource() {
		return "";
	}


	public static CustomTableSource.Builder builder() {
		return new CustomTableSource.Builder();
	}

	public static class Builder<T>{
		private TypeInformation<T> tTypeInformation;
		private String rowTime;
		private long interval = 0;
		private WatermarkStrategy watermarkStrategy;
		private ProduceData produceData;

		public CustomTableSource.Builder setProduceData(ProduceData<?> produceData) {
			this.produceData = produceData;
			return this;
		}

		public CustomTableSource.Builder setTypeInformation(TypeInformation<T> typeInformation) {
			this.tTypeInformation = typeInformation;
			return this;
		}

		public CustomTableSource.Builder setRowTime(String rowTime) {
			this.rowTime = rowTime;
			return this;
		}

		public CustomTableSource.Builder setInterval(long interval) {
			this.interval = interval;
			return this;
		}

		public CustomTableSource.Builder setWatermarkStrategy(WatermarkStrategy watermarkStrategy) {
			this.watermarkStrategy = watermarkStrategy;
			return this;
		}

		protected CustomTableSource.Builder builder(){
			return this;
		}

		public CustomTableSource build(){
			return new CustomTableSource(tTypeInformation,rowTime,interval,watermarkStrategy,produceData);
		}


	}
}
