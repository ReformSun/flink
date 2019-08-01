package com.test.learnMetric;

import com.test.learnMetric.group.GroupUtil;
import com.test.util.StreamExecutionEnvUtil;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.metrics.MetricRegistryConfiguration;
import org.apache.flink.runtime.metrics.MetricRegistryImpl;
import org.apache.flink.runtime.metrics.groups.TaskManagerMetricGroup;
import org.apache.flink.runtime.metrics.util.SystemResourcesMetricsInitializer;

public class TestTaskManagerDetails {
	private static MetricRegistryImpl metricRegistry;
	public static void main(String[] args) {
		Configuration configuration = StreamExecutionEnvUtil.getConfiguration();
		MetricRegistryConfiguration metricRegistryConfiguration = MetricRegistryConfiguration.fromConfiguration(configuration);
		metricRegistry = new MetricRegistryImpl(metricRegistryConfiguration);
		testMethod1();

	}

	public static void testMethod1(){
		TaskManagerMetricGroup taskManagerMetricGroup = GroupUtil.getTaskManagerMetricGroup(metricRegistry);
		SystemResourcesMetricsInitializer.instantiateSystemMetrics(taskManagerMetricGroup, Time.milliseconds(1000));
		for (int i = 0; i < 100; i++) {
			taskManagerMetricGroup.toString();
		}
	}
}
