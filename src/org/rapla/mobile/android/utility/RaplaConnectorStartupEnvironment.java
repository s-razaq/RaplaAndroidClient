/*--------------------------------------------------------------------------*
 | Copyright (C) 2012 Rapla-Team                                            |
 |                                                                          |
 | This program is free software; you can redistribute it and/or modify     |
 | it under the terms of the GNU General Public License as published by the |
 | Free Software Foundation. A copy of the license has been included with   |
 | these distribution in the COPYING file, if not go to www.fsf.org         |
 |                                                                          |
 | As a special exception, you are granted the permissions to link this     |
 | program with every library, which license fulfills the Open Source       |
 | Definition as published by the Open Source Initiative (OSI).             |
 *--------------------------------------------------------------------------*/

package org.rapla.mobile.android.utility;

import java.net.MalformedURLException;
import java.net.URL;

import org.rapla.framework.Configuration;
import org.rapla.framework.DefaultConfiguration;
import org.rapla.framework.RaplaException;
import org.rapla.framework.StartupEnvironment;
import org.rapla.framework.logger.Logger;

/**
 * Startup environment that creates an Facade Object to communicate with an
 * rapla server instance.
 */
public class RaplaConnectorStartupEnvironment implements StartupEnvironment {
	DefaultConfiguration config;
	URL server;
	Logger logger;

	public RaplaConnectorStartupEnvironment(final String host,
			final int hostPort, String contextPath, boolean isSecure,
			final Logger logger) throws MalformedURLException {
		this.logger = logger;

		config = new DefaultConfiguration("rapla-config");
		final DefaultConfiguration facadeConfig = new DefaultConfiguration(
				"facade");
		facadeConfig.setAttribute("id", "facade");
		final DefaultConfiguration remoteConfig = new DefaultConfiguration(
				"remote-storage");
		remoteConfig.setAttribute("id", "remote");

		DefaultConfiguration serverHost = new DefaultConfiguration("server");
		serverHost.setValue("${download-url}");
		remoteConfig.addChild(serverHost);

		config.addChild(facadeConfig);
		config.addChild(remoteConfig);

		String protocoll = "http";
		if (isSecure) {
			protocoll = "https";
		}
		if (!contextPath.startsWith("/")) {
			contextPath = "/" + contextPath;
		}
		if (!contextPath.endsWith("/")) {
			contextPath = contextPath + "/";
		}
		server = new URL(protocoll, host, hostPort, contextPath);
	}

	public Configuration getStartupConfiguration() throws RaplaException {
		return config;
	}

	public int getStartupMode() {
		return EMBEDDED;
	}

	public URL getContextRootURL() throws RaplaException {
		return null;
	}

	public Logger getBootstrapLogger() {
		return logger;
	}

	public URL getDownloadURL() throws RaplaException {
		return server;
	}

	public URL getConfigURL() throws RaplaException {
		return null;
	}

	public URL getLoggerConfigURL() throws RaplaException {
		return null;
	}

};
