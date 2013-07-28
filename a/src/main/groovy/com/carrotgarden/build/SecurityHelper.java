package com.carrotgarden.build;

/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author Andrei Pozolotin
 */
public class SecurityHelper {

	public static void setupNonVerifingSSL() throws Exception {

		final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(final X509Certificate[] arg0,
					final String arg1) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(final X509Certificate[] arg0,
					final String arg1) throws CertificateException {
			}
		} };

		final SSLContext context = SSLContext.getInstance("SSL");

		context.init(null, trustAllCerts, new java.security.SecureRandom());

		final HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(final String hostname,
					final SSLSession session) {
				return true;
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		HttpsURLConnection.setDefaultSSLSocketFactory(context
				.getSocketFactory());

	}

}
