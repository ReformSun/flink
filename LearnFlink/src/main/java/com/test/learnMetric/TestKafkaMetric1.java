package com.test.learnMetric;

import com.test.learnMetric.executiongraph.ExecutionGraphTest;
import com.test.learnMetric.executiongraph.metric.UpTimeGauge;
import com.test.learnMetric.group.GroupUtil;
import com.test.util.StreamExecutionEnvUtil;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.runtime.jobgraph.JobStatus;
import org.apache.flink.runtime.jobgraph.OperatorID;
import org.apache.flink.runtime.metrics.MetricRegistryConfiguration;
import org.apache.flink.runtime.metrics.MetricRegistryImpl;
import org.apache.flink.runtime.metrics.groups.JobManagerJobMetricGroup;
import org.apache.flink.runtime.metrics.groups.OperatorMetricGroup;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartitionState;
import org.apache.flink.streaming.connectors.kafka.internals.metrics.KafkaConsumerMetricConstants;

import static org.apache.flink.streaming.connectors.kafka.internals.metrics.KafkaConsumerMetricConstants.*;

/**
 * 学习kafka测量值数据
 * {@link org.apache.flink.streaming.connectors.kafka.internals.metrics.KafkaConsumerMetricConstants}
 * KafkaConsumer
 * 偏移量提交成功或者失败的测量值
 * 代表的成功个数和失败个数
 * {@link org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase}
 * run 方法中 通过offsetCommitCallback偏移量提交回调
 * commitsSucceeded
 * commitsFailed
 * 通过消费者topic和partition作为分组名
 * topic
 * partition
 * 当前偏移量的测量值
 * currentOffsets
 * 提交偏移量的测量值
 * committedOffsets
 * current-offsets
 * committed-offsets
 *
 *
 */
public class TestKafkaMetric1 {
	private static MetricRegistryImpl metricRegistry;
	public static void main(String[] args) {
		Configuration configuration = StreamExecutionEnvUtil.getConfiguration();
		MetricRegistryConfiguration metricRegistryConfiguration = MetricRegistryConfiguration.fromConfiguration(configuration);
		metricRegistry = new MetricRegistryImpl(metricRegistryConfiguration);

		testMethod1();
//		testMethod2();
//		testMethod3();

	}

	/**
	 * 测试
	 * commitsSucceeded
	 * commitsFailed
	 */
	public static void testMethod1(){
		JobID jobID = new JobID();
		String job_name = "kafka_test_1";
		JobManagerJobMetricGroup jobManagerJobMetricGroup = GroupUtil.getJobManagerJobMetricGroup(metricRegistry,jobID,job_name);
		long[] stateTimestamps = new long[JobStatus.values().length];
		stateTimestamps[JobStatus.RUNNING.ordinal()] = System.currentTimeMillis();
		ExecutionGraphTest executionGraphTest = new ExecutionGraphTest(JobStatus.RUNNING,stateTimestamps);
		jobManagerJobMetricGroup.gauge(UpTimeGauge.METRIC_NAME,new UpTimeGauge(executionGraphTest));
		OperatorMetricGroup operatorMetricGroup = GroupUtil.getOperatorMetricGroup(metricRegistry,jobID,job_name,new OperatorID(),"kafka_source","Source:kafka_source");
		Counter counter1 =  operatorMetricGroup.counter(KafkaConsumerMetricConstants.COMMITS_SUCCEEDED_METRICS_COUNTER);
		Counter counter2 = operatorMetricGroup.counter(KafkaConsumerMetricConstants.COMMITS_FAILED_METRICS_COUNTER);

		for (int i = 0; i < 100000; i++) {
			counter1.inc();
			counter2.inc();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * 通过消费者topic和partition作为分组名
	 * topic
	 * partition
	 * 当前偏移量的测量值
	 * currentOffsets
	 * 提交偏移量的测量值
	 * committedOffsets
	 */
	public static void testMethod2(){
		OperatorMetricGroup operatorMetricGroup = GroupUtil.getOperatorMetricGroup(metricRegistry);
		MetricGroup kafkaConsumerMetricGroup = operatorMetricGroup.addGroup(KafkaConsumerMetricConstants.KAFKA_CONSUMER_METRICS_GROUP);

		for (int i = 0; i < 100; i++) {
			KafkaTopicPartition kafkaTopicPartition = new KafkaTopicPartition("topic",i);
			KafkaTopicPartitionState<?> ktp = new KafkaTopicPartitionState<>(kafkaTopicPartition,null);
			ktp.setOffset(1000);
			ktp.setCommittedOffset(100);
			MetricGroup topicPartitionGroup = kafkaConsumerMetricGroup
				.addGroup(OFFSETS_BY_TOPIC_METRICS_GROUP, ktp.getTopic())
				.addGroup(OFFSETS_BY_PARTITION_METRICS_GROUP, Integer.toString(ktp.getPartition()));
			topicPartitionGroup.gauge(CURRENT_OFFSETS_METRICS_GAUGE, new OffsetGauge(ktp, OffsetGaugeType.CURRENT_OFFSET));
			topicPartitionGroup.gauge(COMMITTED_OFFSETS_METRICS_GAUGE, new OffsetGauge(ktp, OffsetGaugeType.COMMITTED_OFFSET));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}

	/**
	 * topic
	 * partition
	 * current-offsets
	 * committed-offsets
	 */
	public static void testMethod3(){
		OperatorMetricGroup operatorMetricGroup = GroupUtil.getOperatorMetricGroup(metricRegistry);
		MetricGroup kafkaConsumerMetricGroup = operatorMetricGroup.addGroup(KafkaConsumerMetricConstants.KAFKA_CONSUMER_METRICS_GROUP);


		for (int i = 0; i < 100; i++) {
			MetricGroup legacyCurrentOffsetsMetricGroup = kafkaConsumerMetricGroup.addGroup(LEGACY_CURRENT_OFFSETS_METRICS_GROUP);

			MetricGroup legacyCommittedOffsetsMetricGroup = kafkaConsumerMetricGroup.addGroup(LEGACY_COMMITTED_OFFSETS_METRICS_GROUP);

			KafkaTopicPartition kafkaTopicPartition = new KafkaTopicPartition("topic",1);
			KafkaTopicPartitionState<?> ktp = new KafkaTopicPartitionState<>(kafkaTopicPartition,null);
			ktp.setOffset(1000);
			ktp.setCommittedOffset(100);
			legacyCurrentOffsetsMetricGroup.gauge(getLegacyOffsetsMetricsGaugeName(ktp), new OffsetGauge(ktp, OffsetGaugeType.CURRENT_OFFSET));
			legacyCommittedOffsetsMetricGroup.gauge(getLegacyOffsetsMetricsGaugeName(ktp), new OffsetGauge(ktp, OffsetGaugeType.COMMITTED_OFFSET));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}



	}

	private static String getLegacyOffsetsMetricsGaugeName(KafkaTopicPartitionState<?> ktp) {
		return ktp.getTopic() + "-" + ktp.getPartition();
	}


	private static class OffsetGauge implements Gauge<Long> {

		private final KafkaTopicPartitionState<?> ktp;
		private final OffsetGaugeType gaugeType;

		OffsetGauge(KafkaTopicPartitionState<?> ktp, OffsetGaugeType gaugeType) {
			this.ktp = ktp;
			this.gaugeType = gaugeType;
		}

		@Override
		public Long getValue() {
			switch(gaugeType) {
				case COMMITTED_OFFSET:
					return ktp.getCommittedOffset();
				case CURRENT_OFFSET:
					return ktp.getOffset();
				default:
					throw new RuntimeException("Unknown gauge type: " + gaugeType);
			}
		}
	}

	private enum OffsetGaugeType {
		CURRENT_OFFSET,
		COMMITTED_OFFSET
	}

}
