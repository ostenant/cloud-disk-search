package org.ostenant.cloud.search.bdsola.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.ostenant.cloud.search.common.utils.HttpRetryHandler;

@SuppressWarnings("all")
public class BdSolaRetryHandler extends HttpRetryHandler {

	private static Logger logger = Logger.getLogger(BdSolaRetryHandler.class);

	@Override
	public Object handleRetry(HttpResponse httpResponse) {
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (HttpStatus.SC_OK == statusCode) {
			Document doc = null;
			try {
				InputStream in = httpResponse.getEntity().getContent();
				doc = Jsoup.parse(in, "utf-8", "");
				Integer pageBound = BdSolaUtils.pageBound(doc);
				return pageBound;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
