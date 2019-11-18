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
import org.apache.flink.runtime.jobmanager.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 关键点： 调度策略
 *
 *
 *
 * 关键点： 传输策略
 *
 *
 * 关键点： 子任务输出类型
 */
public class TestExecutionGraph1 {

	static Logger logger = LoggerFactory.getLogger(TestExecutionGraph1.class);
	public static void main(String[] args) {
		testMethod1();
	}

	public static void testMethod1(){
		try {
			JobID jobID = new JobID();
			String jobName = "jobName";
			ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);
			ExecutionGraph executionGraph =ExecutionGraphBuilder.buildGraph(null
				,JobGraphUtil.getJobGraph(jobID,jobName)
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
			System.out.println(executionGraph.getAllVertices());
		} catch (JobExecutionException e) {
			e.printStackTrace();
		} catch (JobException e) {
			e.printStackTrace();
		}
	}

}
