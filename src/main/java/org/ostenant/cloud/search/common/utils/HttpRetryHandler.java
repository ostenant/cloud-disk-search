package org.ostenant.cloud.search.common.utils;

import java.io.IOException;
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

@SuppressWarnings("all")
public abstract class HttpRetryHandler {

	private static Logger logger = Logger.getLogger(HttpRetryHandler.class);

	/**
	 * 重新请求url
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public Object executeHttpRetry(String url, final int retryNum)
			throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// 自定义重连机制Handler
		HttpRequestRetryHandler mRequestRetryHandler = new HttpRequestRetryHandler() {

			// 返回true则重试 否则放弃
			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (executionCount > retryNum) {
					// 如果超过最大重试次数，那么就不要继续了
					logger.info(" >> 连接超时.....");
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					// 如果服务器丢掉了连接，那么就重试
					logger.info(" >> 服务器丢掉了连接.....");
					return true;
				}
				if (exception instanceof SSLHandshakeException) {
					// 不要重试SSL握手异常
					logger.info(" >> 不要重试SSL握手异常.....");
					return false;
				}

				logger.info("executionCount ==>" + executionCount);

				HttpRequest request = (HttpRequest) context
						.getAttribute(ExecutionContext.HTTP_REQUEST);

				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// 如果请求被认为是幂等的，那么就重试
					logger.info(" >> 请求被认为是幂等的.....");
					return true;
				}

				return false;
			}
		};

		// 设置Http重连机制
		httpClient.setHttpRequestRetryHandler(mRequestRetryHandler);
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpGet);

		return handleRetry(httpResponse);

	}

	public abstract Object handleRetry(HttpResponse httpResponse);

}
