package org.osscape.loader;

import org.osscape.parameters.OSParameters;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ethan on 7/7/2017.
 */
public class LoaderStub implements AppletStub {
	OSParameters params;

	public LoaderStub(OSParameters params) {
		this.params = params;
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public URL getDocumentBase() {
		return getCodeBase();
	}

	@Override
	public URL getCodeBase() {
		try {
			return new URL("http://93.158.237.2");
		} catch (MalformedURLException e) {

		}
		return null;
	}

	@Override
	public String getParameter(String key) {
		return params.prop.getProperty(key);
	}

	@Override
	public AppletContext getAppletContext() {
		return null;
	}

	@Override
	public void appletResize(int w, int h) {

	}

}
