package com.test.io.netty;

import org.apache.flink.runtime.io.network.partition.ResultPartition;
import org.apache.flink.runtime.io.network.partition.consumer.SingleInputGate;
import org.apache.flink.runtime.taskmanager.Task;
import org.apache.flink.runtime.taskmanager.TaskManager;

import java.io.IOException;

/**
 * Network I/O components of each {@link TaskManager} instance. The network environment contains
 * the data structures that keep track of all intermediate results and all data exchanges.
 * 每一个的任务管理者实例的网络I/O组件。这个网络环境包含追踪的所有中间结果和全部数据交换的数据结构
 * {@link org.apache.flink.runtime.io.network.NetworkEnvironment}
 *
 * 重要属性： {@link org.apache.flink.runtime.io.disk.iomanager.IOManager.IOMode}
 * 决定I/O模型 异步或者同步
 *
 */
public class TestNetworkEnvironment {

	/**
	 * 重要方法
	 *  获取任务中所有的 被生产的分区{@link org.apache.flink.runtime.io.network.partition.ResultPartition}
	 *  遍历结果分区，设置每个结果分区setupPartition
	 *
	 *  获取任务中所有的 输入入口{@link org.apache.flink.runtime.io.network.partition.consumer.SingleInputGate}
	 *  遍历输入入口，设置每个输入入口setupInputGate
	 *
	 */
	public void registerTask(Task task) throws IOException {

	}

	/**
	 * 为结果分区创建一个缓冲区
	 * @param partition
	 * @throws IOException
	 */
	public void setupPartition(ResultPartition partition) throws IOException {

	}

	public void setupInputGate(SingleInputGate gate) throws IOException {

	}

}
