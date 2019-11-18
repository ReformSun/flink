package com.test.filesystem;

import java.io.File;
import java.io.IOException;

public class TestFsDataOutputStream {
	private static String basePath = "/Users/apple/Documents/GitHub/flink-1.8/LearnFlink/src/main/resources/parquet/";
	public static void main(String[] args) throws IOException {
		testMethod1();
	}
	public static void testMethod1() throws IOException {
		File targetFile = new File(basePath + "target/test.txt");
		File tempFile = new File(basePath+"tmp");
		CustomLocalRecoverableFsDataOutputStream customLocalRecoverableFsDataOutputStream = new CustomLocalRecoverableFsDataOutputStream(targetFile,tempFile);
		customLocalRecoverableFsDataOutputStream.write("ddddddd".getBytes());
		customLocalRecoverableFsDataOutputStream.closeForCommit().commit();

	}
}
