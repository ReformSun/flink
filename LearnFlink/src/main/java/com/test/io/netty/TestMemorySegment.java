package com.test.io.netty;

import org.apache.flink.core.memory.MemorySegment;
import org.apache.flink.core.memory.MemorySegmentFactory;

import java.io.*;

public class TestMemorySegment {
	public static void main(String[] args) {
//		testMethod1();
//		testMethod2();
		testMethod3();
	}
	public static void testMethod1(){
		MemorySegment memorySegment = MemorySegmentFactory.allocateUnpooledSegment(100);
		String src = "dddd";
		memorySegment.put(1,src.getBytes());
		byte[] bytes = new byte[src.length()];
		memorySegment.get(1,bytes);
		System.out.println(new String(bytes));
	}

	/**
	 * 返回堆外内存的地址
	 */
	public static void testMethod2(){
		MemorySegment memorySegment = MemorySegmentFactory.allocateUnpooledSegment(100);
		String src = "dddd";
		memorySegment.put(1,src.getBytes());
		System.out.println(memorySegment.getAddress());
	}

	public static void testMethod3(){
		InputStream inputStream = System.in;
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		MemorySegment memorySegment = MemorySegmentFactory.allocateUnpooledSegment(10000);
		try {
			memorySegment.put(dataInputStream,0,1000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread thread = new Thread(()->{
			System.out.println("ccc");
			while (memorySegment.isFreed()){
				try {
					memorySegment.get(new DataOutputStream(System.out),0,1000);
					Thread.sleep(100);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
		thread.start();
		System.out.println("ddd");

	}
}
