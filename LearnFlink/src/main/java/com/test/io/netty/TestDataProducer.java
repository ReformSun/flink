package com.test.io.netty;

/**
 * 数据生产的逻辑
 * {@link org.apache.flink.runtime.io.network.api.writer.RecordWriter}
 * 首先数据会通过RecordWriter类的emit方法进行发送把发送记录和选择的通道传入
 * 然后对记录进行序列化，然后拷贝序列化数据到指定通道调用
 * copyFromSerializerToTargetChannel方法 执行详情进入RecordWriter类中查看
 * 然后通过{@link org.apache.flink.runtime.io.network.partition.ResultPartition}
 * 这个类是一个主要的类
 *
 *
 */
public class TestDataProducer {

	/**
	 *
	 * 配置信息：{@link org.apache.flink.configuration.TaskManagerOptions}
	 * {@link org.apache.flink.runtime.taskexecutor.TaskManagerServicesConfiguration}
	 *
	 * 缓存池的大小设置逻辑
	 * {@link org.apache.flink.runtime.io.network.NetworkEnvironment}
	 * 通过registerTask方法注册Task任务
	 * 通过setupPartition方法中设置结果缓冲区
	 *
	 *
	 */
	public static void testMethod1(){

	}

}
