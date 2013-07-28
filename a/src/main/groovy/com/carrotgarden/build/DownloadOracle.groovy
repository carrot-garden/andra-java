package com.carrotgarden.build
import org.apache.commons.io.IOUtils


/**
 * wget --no-cookies --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com"
 * "http://download.oracle.com/otn-pub/java/jdk/7/jdk-7-linux-x64.tar.gz"
 */
class DownloadOracle {

	private def HttpURLConnection makeConnection(final String locationURL) {

		final URL url = new URL(locationURL)

		final HttpURLConnection connection = url.openConnection()

		connection.setUseCaches(false);
		connection.setConnectTimeout(10 * 1000);
		connection.setInstanceFollowRedirects(true);

		for (final Map.Entry<String, String> entry : headerMap.entrySet()) {
			connection.setRequestProperty(entry.getKey(), entry.getValue());
		}

		connection.connect();

		return connection;
	}

	def execute() {

		SecurityHelper.setupNonVerifingSSL();

		println "javaRemoteURL: ${javaRemoteURL}"
		println "javaLocalURL : ${javaLocalURL}"

		final File file = new File(javaLocalURL);

		if (!javaEveryTime && file.exists()) {
			println "Java artifact is present, skip download."
			return;
		} else {
			println "Java artifact is missing, make download."
		}

		file.getParentFile().mkdirs();

		HttpURLConnection connection = makeConnection(javaRemoteURL)

		while (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
			connection = makeConnection(connection.getHeaderField("Location"));
			println "redirect: ${connection}"
		}

		final ProgressInputStream input = new ProgressInputStream(
				connection.getInputStream(), connection.getContentLengthLong());

		input.addPropertyChangeListener(new ProgressChangeListener());

		final OutputStream output = new FileOutputStream(file);

		IOUtils.copy(input, output);

		IOUtils.closeQuietly(input);
		IOUtils.closeQuietly(output);

		if (file.length() < 1000 * 1000) {
			throw new IllegalStateException("Download failure.");
		}

		println "Java artifact downloaded: ${file.length()} bytes."
	}

	String javaRemoteURL
	String javaLocalURL
	Boolean javaEveryTime

	Map<String, String> headerMap = new HashMap<String, String>()
}
