package com.test.io.netty;

/**
 * 连接管理类的学习
 * The connection manager manages physical connections for the (logical) remote input channels at runtime.
 * 连接管理者在运行时管理远程输入通道的物理连接
 * 接口功能：
 * start   开始
 * createPartitionRequestClient 创建分区请求客户端
 * getNumberOfActiveConnections 得到活跃的连接数
 * closeOpenChannelConnections  关闭打开的通道连接
 * getDataPort   得到数据端口
 * shutdown  停止
 */
public class TestConnectionManager {
	public static void main(String[] args) {

	}

	/**
	 * 创建流程
	 * {@link org.apache.flink.runtime.taskexecutor.TaskManagerServices}
	 * fromConfiguration() 静态方法，根据配置文件创建任务管理服务
	 *
	 * createNetworkEnvironment() 创建网络环境{
	 * 根据网络配置文件{@link org.apache.flink.runtime.taskmanager.NetworkEnvironmentConfiguration}
	 * 创建网络连接池{@link org.apache.flink.runtime.io.network.buffer.NetworkBufferPool}
	 *
	 *  更加配置文件{@link org.apache.flink.runtime.io.network.netty.NettyConfig}是否为空创建连接管理者
	 * 	分为netty连接管理者{@link org.apache.flink.runtime.io.network.netty.NettyConnectionManager}
	 * 	或者本地连接管理者{@link org.apache.flink.runtime.io.network.LocalConnectionManager}
	 *
	 * 	开始netty连接管理者，通过ResultPartitionManager和TaskEventDispatcher
	 *
	 * 	详情分析进入{@link org.apache.flink.runtime.io.network.netty.NettyConnectionManager} start方法查看
	 *
	 * }
	 */
	public static void testMethod1(){

	}

	/**
	 * The result partition manager keeps track of all currently produced/consumed partitions of a task manager.
	 * 结果分区管理者保持追踪任务管理器当前所有的被生产或者被消费的分区
	 * {@link org.apache.flink.runtime.io.network.partition.ResultPartitionManager}
	 */
	public static void testMethod2(){
	}

	/**
	 * The task event dispatcher dispatches events flowing backwards from a consuming task to the task producing the consumed result.
	 * 这个任务事件调度这调度事件从一个正在消费的任务到一个正在生产这个已经被消费的结果，平滑的向后
	 * {@link org.apache.flink.runtime.io.network.TaskEventDispatcher}
	 */
	public static void testMethod3(){

	}
}
