package com.test.filesystem;

import org.apache.flink.core.fs.RecoverableWriter;

import java.io.File;

import static org.apache.flink.util.Preconditions.checkArgument;
import static org.apache.flink.util.Preconditions.checkNotNull;

public class LocalRecoverable implements RecoverableWriter.CommitRecoverable, RecoverableWriter.ResumeRecoverable {
	/** The file path for the final result file. */
	private final File targetFile;

	/** The file path of the staging file. */
	private final File tempFile;

	/** The position to resume from. */
	private final long offset;

	/**
	 * Creates a resumable for the given file at the given position.
	 *
	 * @param targetFile The file to resume.
	 * @param offset The position to resume from.
	 */
	LocalRecoverable(File targetFile, File tempFile, long offset) {
		checkArgument(offset >= 0, "offset must be >= 0");
		this.targetFile = checkNotNull(targetFile, "targetFile");
		this.tempFile = checkNotNull(tempFile, "tempFile");
		this.offset = offset;
	}

	public File targetFile() {
		return targetFile;
	}

	public File tempFile() {
		return tempFile;
	}

	public long offset() {
		return offset;
	}

	@Override
	public String toString() {
		return "LocalRecoverable " + tempFile + " @ " + offset + " -> " + targetFile;
	}
}
