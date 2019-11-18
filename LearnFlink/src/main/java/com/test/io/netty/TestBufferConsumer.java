package com.test.io.netty;

import org.apache.flink.core.memory.MemorySegment;
import org.apache.flink.core.memory.MemorySegmentFactory;
import org.apache.flink.runtime.io.network.buffer.BufferBuilder;
import org.apache.flink.runtime.io.network.buffer.BufferConsumer;
import org.apache.flink.runtime.io.network.buffer.FreeingBufferRecycler;

import java.nio.ByteBuffer;

/**
 * flink 网络层以上的处理逻辑
 * {@link org.apache.flink.runtime.io.network.api.writer.RecordWriter}
 * 负责当前节点向下一级节点写数据
 * RecordWriter中重要的属性：ChannelSelector {@link org.apache.flink.runtime.io.network.api.writer.ChannelSelector}
 * 选择对应的网络通道，进行数据传输。
 * RecordWriter中重要的属性：BufferBuilder {@link org.apache.flink.runtime.io.network.buffer.BufferBuilder}
 * 进行数据的缓存
 * RecordWriter中重要的属性： ecordSerializer {@link org.apache.flink.runtime.io.network.api.serialization.RecordSerializer}
 * 进行数据的序列化
 *
 * 当数据进行emit发射时，数据会首先进行序列化
 * 然后copy拷贝数据到目标通道
 *
 *
 */
public class TestBufferConsumer {
	public static void main(String[] args) {
		testMethod2();
	}

	public static void testMethod1(){
		MemorySegment memorySegment = MemorySegmentFactory.allocateUnpooledSegment(100);
		BufferConsumer bufferConsumer = new BufferConsumer(memorySegment, FreeingBufferRecycler.INSTANCE,true);
	}

	/**
	 * 添加数据之前一定要调用flip方法
	 * 把limit设置成当前位置，position设置为0
	 */
	public static void testMethod2(){
		MemorySegment memorySegment = MemorySegmentFactory.allocateUnpooledSegment(100);
		BufferBuilder bufferBuilder = new BufferBuilder(memorySegment, FreeingBufferRecycler.INSTANCE);
		BufferConsumer bufferConsumer = bufferBuilder.createBufferConsumer();
		ByteBuffer byteBuffer = ByteBuffer.allocate(100);
		String src = "ddddd";
		byteBuffer.put(src.getBytes());
		byteBuffer.flip();
		bufferBuilder.append(byteBuffer);

		ByteBuffer byteBuffer1 = ByteBuffer.allocate(100);
		String src1 = "ddddd";
		byteBuffer1.put(src.getBytes());
		byteBuffer1.flip();
		bufferBuilder.append(byteBuffer1);


		byte[] bytes = new byte[src.length() + src1.length()];
//		int start = bufferConsumer.getWrittenBytes() - src.length();
//		int start = bufferConsumer.getWrittenBytes();
		int start = 0;
		bufferConsumer.build().getMemorySegment().get(start,bytes);
		System.out.println(new String(bytes));
	}
}
