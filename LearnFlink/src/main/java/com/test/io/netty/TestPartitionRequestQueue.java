package com.test.io.netty;

import org.apache.flink.runtime.io.network.buffer.Buffer;

/**
 * A nonEmptyReader of partition queues, which listens for channel writability changed
 * events before writing and flushing {@link Buffer} instances.
 * 一个分区队列的非空读取器，监听在正在写或者冲刷缓冲区实例的通道可写性改变时间
 *
 * {@link org.apache.flink.runtime.io.network.netty.PartitionRequestQueue}
 * 分区请求队列
 */
public class TestPartitionRequestQueue {
}
