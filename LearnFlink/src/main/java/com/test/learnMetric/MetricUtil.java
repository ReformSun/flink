package com.test.learnMetric;

import com.test.learnMetric.group.GroupUtil;
import com.test.util.StreamExecutionEnvUtil;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Metric;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.runtime.metrics.MetricRegistryConfiguration;
import org.apache.flink.runtime.metrics.MetricRegistryImpl;
import org.apache.flink.runtime.metrics.groups.JobManagerJobMetricGroup;

public class MetricUtil {
	private static MetricRegistryImpl metricRegistry;
	public static MetricGroup getMetric(JobID jobID,String job_name){
		Configuration configuration = StreamExecutionEnvUtil.getConfiguration();
		MetricRegistryConfiguration metricRegistryConfiguration = MetricRegistryConfiguration.fromConfiguration(configuration);
		metricRegistry = new MetricRegistryImpl(metricRegistryConfiguration);
		JobManagerJobMetricGroup jobManagerJobMetricGroup = GroupUtil.getJobManagerJobMetricGroup(metricRegistry,jobID,job_name);
		return jobManagerJobMetricGroup;
	}
}
