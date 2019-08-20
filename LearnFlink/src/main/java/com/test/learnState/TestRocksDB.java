package com.test.learnState;

import org.rocksdb.NativeLibraryLoader;
import org.rocksdb.RocksDB;

import java.io.File;
import java.io.IOException;

public class TestRocksDB {
	public static void main(String[] args) {
		testMethod1();
	}

	public static void testMethod1(){
		System.out.println(System.getProperty("java.library.path"));
//		File file = new File("/Users/apple/Desktop/rocksdbjni-5.8.6.jar.src/librocksdbjni-osx.jnilib");
//		System.load(file.getAbsolutePath());
//		System.loadLibrary("librocksdbjni");
//		System.setProperty("java.library.path","/Users/apple/Desktop/rocksdbjni-5.8.6.jar.src/:.");

		try {
			NativeLibraryLoader.getInstance().loadLibrary("/Users/apple/Desktop/rockdata");
		} catch (IOException e) {
			e.printStackTrace();
		}

//		RocksDB.loadLibrary();
	}
}
