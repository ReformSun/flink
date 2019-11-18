package com.test.graph;

import com.test.learnMetric.MetricUtil;
import com.test.util.StreamExecutionEnvUtil;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.JobException;
import org.apache.flink.runtime.blob.VoidBlobWriter;
import org.apache.flink.runtime.client.JobExecutionException;
import org.apache.flink.runtime.executiongraph.ExecutionGraph;
import org.apache.flink.runtime.executiongraph.ExecutionGraphBuilder;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobmanager.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ExecutionGraphUtil {
	static Logger logger = LoggerFactory.getLogger(ExecutionGraphUtil.class);
	public static ExecutionGraph getExecutionGraph(JobGraph jobGraph,JobID jobID,String jobName){
		try {
			ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);
			ExecutionGraph executionGraph = ExecutionGraphBuilder.buildGraph(null
				,jobGraph
				, StreamExecutionEnvUtil.getConfiguration()  // 配置信息
				,scheduledThreadPoolExecutor     // 执行器
				, Executors.newSingleThreadExecutor()        // 执行器
				,new Scheduler(Executors.newSingleThreadExecutor())       // 槽提供者
				,TestExecutionGraph1.class.getClassLoader()       // 类加载器
				,null    // 恢复工厂
				, Time.minutes(1)        // 延迟时间设置
				,null     // 重新启动存储
				, MetricUtil.getMetric(jobID,jobName)           // 测量值
				, VoidBlobWriter.getInstance()         // 写
				,Time.minutes(1)
				,logger);
			return executionGraph;
		} catch (JobExecutionException e) {
			e.printStackTrace();
		} catch (JobException e) {
			e.printStackTrace();
		}
		return null;
	}
}
