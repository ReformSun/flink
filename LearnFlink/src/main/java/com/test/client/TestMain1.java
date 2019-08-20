package com.test.client;

import org.apache.flink.client.cli.CliFrontend;
import org.apache.flink.client.cli.CustomCommandLine;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.runtime.security.SecurityConfiguration;
import org.apache.flink.runtime.security.SecurityUtils;
import org.apache.flink.runtime.util.EnvironmentInformation;
import org.apache.flink.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

import static org.apache.flink.client.cli.CliFrontend.getConfigurationDirectoryFromEnv;
import static org.apache.flink.client.cli.CliFrontend.loadCustomCommandLines;

public class TestMain1 {
	private static final Logger LOG = LoggerFactory.getLogger(TestMain1.class);
	/**
	 * Submits the job based on the arguments.
	 */
	public static void main(final String[] arg) {
		String[] args = new String[2];
		args[0] = "run";
		args[1] = "/Users/apple/Documents/GitHub/flink-1.8/LearnFlink/target/test.jar";
		EnvironmentInformation.logEnvironmentInfo(LOG, "Command Line Client", args);

		// 1. find the configuration directory
		final String configurationDirectory = getConfigurationDirectoryFromEnv();

		// 2. load the global configuration
		final Configuration configuration = GlobalConfiguration.loadConfiguration(configurationDirectory);


		// 3. load the custom command lines
		final List<CustomCommandLine<?>> customCommandLines = loadCustomCommandLines(
			configuration,
			configurationDirectory);

		try {
			final CliFrontend cli = new CliFrontend(
				configuration,
				customCommandLines);

			SecurityUtils.install(new SecurityConfiguration(configuration));
			int retCode = SecurityUtils.getInstalledContext()
				.runSecured(() -> cli.parseParameters(args));
			System.exit(retCode);
		}
		catch (Throwable t) {
			final Throwable strippedThrowable = ExceptionUtils.stripException(t, UndeclaredThrowableException.class);
			LOG.error("Fatal error while running command line interface.", strippedThrowable);
			strippedThrowable.printStackTrace();
			System.exit(31);
		}
	}

	public static String getConfigurationDirectoryFromEnv() {
		String location = "/Users/apple/Documents/GitHub/flink-1.8/LearnFlink/src/main/resources/config";
		return location;
	}
}
