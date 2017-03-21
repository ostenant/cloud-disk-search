package org.ellis.yun.search.test.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Test;

@SuppressWarnings("all")
public class HttpClientTest {

	private static final String URL1 = "http://pan.baidu.com/share/link?shareid=44781070&uk=2067647762";
	private static final String ERR_URL1 = "http://www.dsafasdadada.com/";

	/**
	 * HTTP设置请求编码
	 * <p>
	 * 第一种方法： 在Hedaer中设置Content-Type
	 * </p>
	 * HttpGet httpGet = new HttpGet(url); httpGet.addHeader("Content-Type",
	 * "text/html;charset=UTF-8");
	 * 
	 * 
	 * <p>
	 * 第二种方法： 设置HttpClient的CONTENT_CHARSET
	 * </p>
	 * HttpClient httpClient = new DefaultHttpClient();
	 * httpClient.getParams().setParameter(
	 * CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
	 * 
	 * 
	 * <p>
	 * 第三种方法： 设置get/post method的CONTENT_CHARSET
	 * </p>
	 * HttpGet httpGet = new HttpGet(url);
	 * httpGet.getParams().setParameter(CoreProtocolPNames.
	 * HTTP_CONTENT_CHARSET, "UTF-8");
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHttpGet() throws Exception {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"https://github.com/alibaba/dubbo/archive/dubbo-2.4.11.zip");

		httpClient.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");

		HttpResponse httpResponse = httpClient.execute(httpGet);

		HttpEntity entity = httpResponse.getEntity();
		String content = parseEntity(entity);
		System.out.println(content);

	}

	private String parseEntity(HttpEntity entity) throws IOException,
			UnsupportedEncodingException {
		if (entity != null) {
			InputStream in = entity.getContent();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			IOUtils.copy(in, out);

			byte[] bytes = out.toByteArray();
			if (bytes.length > 0) {
				String content = new String(bytes, "UTF-8");
				return content;
			}
		}
		return null;
	}

	@Test
	public void testCreateURL() throws Exception {

		List<NameValuePair> queryPairs = new ArrayList<NameValuePair>();

		queryPairs.add(new BasicNameValuePair("shareid", "44781070"));
		queryPairs.add(new BasicNameValuePair("uk", "2067647762"));
		@SuppressWarnings("deprecation")
		URI uri = URIUtils.createURI("http", "pan.baidu.com", -1,
				"/share/link", URLEncodedUtils.format(queryPairs, "UTF-8"),
				null);
		System.out.println(uri.toString());

	}

	@Test
	public void testHttpResponse() throws Exception {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
				HttpStatus.SC_OK, "OK");
		System.out.println(response.getProtocolVersion() + "");
		System.out.println(response.getStatusLine().getStatusCode() + "");
		System.out.println(response.getStatusLine().getReasonPhrase());
		System.out.println(response.getStatusLine().toString());

		response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
		response.addHeader("Set-Cookie",
				"c2=b; path=\"/\", c3=c; domain=\"localhost\"");
		HeaderElementIterator it = new BasicHeaderElementIterator(
				response.headerIterator("Set-Cookie"));

		while (it.hasNext()) {
			HeaderElement elem = it.nextElement();
			System.out.println(elem.getName() + " = " + elem.getValue());
			NameValuePair[] params = elem.getParameters();
			for (int i = 0; i < params.length; i++) {
				System.out.println(" " + params[i]);
			}
		}
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testStringEntity() throws Exception {
		StringEntity myEntity = new StringEntity("important message", "UTF-8");
		System.out.println(myEntity.getContentType() + "");
		System.out.println(myEntity.getContentLength() + "");
		System.out.println(EntityUtils.getContentCharSet(myEntity));
		System.out.println(EntityUtils.toString(myEntity));
		System.out.println(EntityUtils.toByteArray(myEntity).length + "");
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testFileEntity() throws Exception {
		File file = new File("src/test/resources/README.txt");
		FileEntity entity = new FileEntity(file,
				"text/plain; charset=\"UTF-8\"");
		System.out.println(entity.getContentLength() + "");
		System.out.println(entity.getContentType() + "");
		String content = parseEntity(entity);
		System.out.println(content);
	}

	/**
	 * http post琛ㄥ崟鎻愪氦鏁版嵁
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUrlEncodedFormEntity() throws Exception {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("param1", "value1"));
		formparams.add(new BasicNameValuePair("param2", "value2"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
				"UTF-8");

		String content = parseEntity(entity);
		System.out.println(content);
	}

	@Test
	public void testEntityChunk() throws Exception {
		File file = new File("src/test/resources/README.txt");
		@SuppressWarnings("deprecation")
		FileEntity entity = new FileEntity(file,
				"text/plain; charset=\"UTF-8\"");
		entity.setChunked(true);
		System.out.println(entity.isChunked() + "");
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testResponseHandler() throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL1);
		HttpContext context = new BasicHttpContext();

		httpGet.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");

		System.out.println("Go Aready...");

		HttpRequestInterceptor requestInterceptor = new HttpRequestInterceptor() {

			public void process(HttpRequest request, HttpContext context)
					throws HttpException, IOException {
				System.out.println("---------------------------------------");

				System.out.println("Request encoding... "
						+ request.getParams().getParameter(
								CoreProtocolPNames.HTTP_CONTENT_CHARSET));
			}
		};

		HttpResponseInterceptor responseInterceptor = new HttpResponseInterceptor() {

			public void process(HttpResponse response, HttpContext context)
					throws HttpException, IOException {
				System.out.println("---------------------------------------");

				System.out.println("Response encoding... "
						+ response.getParams().getParameter(
								CoreProtocolPNames.HTTP_CONTENT_CHARSET));

			}
		};

		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == HttpStatus.SC_OK) {
					Header[] allHeaders = response.getAllHeaders();
					if (allHeaders.length > 0) {
						for (Header header : allHeaders) {
							String name = header.getName();
							String value = header.getValue();
							System.out.println(name + " = " + value);
						}
						System.out
								.println("-----------------------------------");
					}

					String charset = "UTF-8";
					HttpEntity entity = response.getEntity();
					Header encoding = entity.getContentEncoding();
					if (encoding != null) {
						charset = encoding.getValue();
					}
					System.out.println("charset ==>" + charset);

					if (entity != null) {
						InputStream in = entity.getContent();
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						IOUtils.copy(in, out);

						byte[] bytes = out.toByteArray();
						if (bytes.length > 0) {
							String content = new String(bytes, charset);
							return content;
						}
					}
					return null;

				} else {
					return statusCode + " | " + statusLine.getReasonPhrase();
				}
			}
		};

		httpclient.addRequestInterceptor(requestInterceptor);
		httpclient.addResponseInterceptor(responseInterceptor);
		String content = httpclient.execute(httpGet, responseHandler, context);

		System.out.println(content);
	}

	/**
	 * 在HTTP请求执行的这一过程中，HttpClient添加了下列属性到执行上下文中<br>
	 * 'http.connection'：HttpConnection实例代表了连接到目标服务器的真实连接。 
	 * 'http.target_host'：HttpHost实例代表了连接目标。 
	 * 'http.proxy_host'：如果使用了，HttpHost实例代表了代理连接。 
	 * 'http.request'：HttpRequest实例代表了真实的HTTP请求。 
	 * 'http.response'：HttpResponse实例代表了真实的HTTP响应。 
	 * 'http.request_sent'：java.lang.Boolean对象代表了暗示真实请求是否被完全传送到目标连接的标识。
	 * 
	 * @throws Exception
	 */
	@Test
	@SuppressWarnings("deprecation")
	public void testHttpContext() throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// httpContext 用于保存httpClient上下文连续的http请求重用
		HttpContext httpContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(URL1);

		HttpResponse response = httpclient.execute(httpGet, httpContext);

		ManagedClientConnection connection = (ManagedClientConnection) httpContext
				.getAttribute(ExecutionContext.HTTP_CONNECTION);
		System.out.println("LocalAddr ==> "
				+ connection.getLocalAddress().getHostAddress());
		System.out.println("LocalPort ==> " + connection.getLocalPort());
		System.out.println("RemoteAddr ==> "
				+ connection.getRemoteAddress().getHostAddress());
		System.out.println("RemotePort ==> " + connection.getRemotePort());
		System.out.println("isOpen ==> " + connection.isOpen());
		System.out.println("isSecure ==> " + connection.isSecure());
		System.out.println("isStale ==> " + connection.isStale());

		HttpHost targetHost = (HttpHost) httpContext
				.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
		System.out.println(targetHost.toURI());

		// HttpHost proxyHost = (HttpHost) httpContext
		// .getAttribute(ExecutionContext.HTTP_PROXY_HOST);

		// 获取上下文信息
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String content = parseEntity(entity);
			System.out.println(content);
			// 会关闭输入流
			entity.consumeContent();
		}

		connection.abortConnection();
		connection.shutdown();

	}

	/**
	 * 请求重试处理<br>
	 * 幂等操作的额外执行不会对二者产生危害性后果
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHttpRetry() throws Exception {
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
				if (executionCount > 5) {
					// 如果超过最大重试次数，那么就不要继续了
					System.out.println("连接超时.....");
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException) {
					// 不要重试SSL握手异常
					return false;
				}

				System.out.println("executionCount ==>" + executionCount);

				HttpRequest request = (HttpRequest) context
						.getAttribute(ExecutionContext.HTTP_REQUEST);

				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// 如果请求被认为是幂等的，那么就重试
					return true;
				}

				return false;
			}
		};

		// 设置Http重连机制
		httpClient.setHttpRequestRetryHandler(mRequestRetryHandler);

		HttpGet httpGet = new HttpGet(new URI(ERR_URL1));

		HttpResponse httpResponse = httpClient.execute(httpGet);

		String content = parseEntity(httpResponse.getEntity());

		System.out.println(content);

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSSLConnection() throws Exception {
		Scheme http = new Scheme("http", PlainSocketFactory.getSocketFactory(),
				80);
		SSLSocketFactory ssf = new SSLSocketFactory(
				SSLContext.getInstance("TLS"));
		ssf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
		Scheme https = new Scheme("https", ssf, 443);
		SchemeRegistry sr = new SchemeRegistry();
		sr.register(http);
		sr.register(https);

		TrustManager easyTrustManager = new X509TrustManager() {

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1) {
				System.out.println("checkClientTrusted");
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1) {
				System.out.println("checkServerTrusted");
			}

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				System.out.println("getAcceptedIssuers");
				return null;
			}
		};

		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(null, new TrustManager[] { easyTrustManager }, null);
		SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
		SSLSocket socket = (SSLSocket) sf.createSocket();
		socket.setEnabledCipherSuites(new String[] { "SSL_RSA_WITH_RC4_128_MD5" });
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
		sf.connectSocket(socket, "119.29.234.42", 443, null, -1, params);
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testConnectionManagerAndProxy() throws Exception {

		HttpParams params = new BasicHttpParams();

		Scheme http = new Scheme("http", PlainSocketFactory.getSocketFactory(),
				80);
		SchemeRegistry sr = new SchemeRegistry();
		sr.register(http);

		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, sr);

		DefaultHttpClient httpClient = new DefaultHttpClient(cm, params);

		// 方式一 设置HTTP代理
		String proxyHost = "proxy.wdf.sap.corp";
		int proxyPort = 8080;
		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(proxyHost, proxyPort),
				new UsernamePasswordCredentials("", ""));
		HttpHost proxy = new HttpHost(proxyHost, proxyPort);
		httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
				proxy);

		// 方式二 设置HTTP代理
		HttpRoutePlanner routePlanner = new HttpRoutePlanner() {

			public HttpRoute determineRoute(HttpHost target,
					HttpRequest request, HttpContext context)
					throws HttpException {
				return new HttpRoute(target, null, new HttpHost(
						"proxy.wdf.sap.corp", 8080), true);
			}
		};

		// httpClient.setRoutePlanner(routePlanner);

		String[] urisToGet = { "http://119.29.234.42/",//
				"http://119.29.234.42/solr",//
				"http://119.29.234.42/jenkins", //
				"http://119.29.234.42/jenkins/manage",//
				"http://119.29.234.42/jenkins/credential-store", //
		};

		// 为每个URI创建一个线程
		GetThread[] threads = new GetThread[urisToGet.length];
		for (int i = 0; i < threads.length; i++) {
			HttpGet httpget = new HttpGet(urisToGet[i]);
			threads[i] = new GetThread(httpClient, httpget);
		}

		// 开始执行线程
		for (int j = 0; j < threads.length; j++) {
			threads[j].start();
		}
		// 合并线程
		for (int j = 0; j < threads.length; j++) {
			threads[j].join();
		}

		cm.closeIdleConnections(40, TimeUnit.SECONDS);
		cm.closeExpiredConnections();

	}

	static class GetThread extends Thread {
		private final HttpClient httpClient;
		private final HttpContext context;
		private final HttpGet httpget;

		public GetThread(HttpClient httpClient, HttpGet httpget) {
			this.httpClient = httpClient;
			this.context = new BasicHttpContext();
			this.httpget = httpget;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			try {
				HttpResponse response = this.httpClient.execute(this.httpget,
						this.context);

				StatusLine statusLine = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// 对实体做些有用的事情...
					// 保证连接能释放回管理器
					System.out.println(this.getName() + " >>> "
							+ statusLine.toString());

					entity.consumeContent();
				}
			} catch (Exception ex) {
				this.httpget.abort();
			}
		}
	}

	@Test
	public void testKeepAliveTime() throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// httpContext 用于保存httpClient上下文连续的http请求重用
		HttpContext httpContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		httpclient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {

			public long getKeepAliveDuration(HttpResponse response,
					HttpContext context) {

				// 先获取Keep-Alive头信息
				HeaderIterator hit = response
						.headerIterator(HTTP.CONN_KEEP_ALIVE);
				HeaderElementIterator it = new BasicHeaderElementIterator(hit);
				while (it.hasNext()) {
					HeaderElement element = it.nextElement();
					String name = element.getName();
					String value = element.getValue();
					if ("timeout".equalsIgnoreCase(name)
							&& !StringUtils.isBlank(value)) {
						return Long.parseLong(value) * 1000;
					}
				}

				HttpHost target = (HttpHost) context
						.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

				if ("www.baidu.com".equalsIgnoreCase(target.getHostName())) {
					// 只保持活动5秒
					return 5 * 1000;
				} else {
					// 否则保持活动30秒
					return 30 * 1000;
				}
			}
		});

		HttpResponse response = httpclient.execute(httpGet, httpContext);
		String content = parseEntity(response.getEntity());
		System.out.println(content);

		System.out.println("=============================================\n");

		// 先获取Keep-Alive头信息
		HeaderIterator hit = response.headerIterator();
		HeaderElementIterator it = new BasicHeaderElementIterator(hit);
		while (it.hasNext()) {
			HeaderElement element = it.nextElement();
			String name = element.getName();
			String value = element.getValue();
			System.out.println(" ==> " + name + " >> " + value);
		}
	}

	/**
	 * http://pan.baidu.com/s/1eSfkuTc
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFetchLicense() throws Exception {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpContext context = new BasicHttpContext();
		String uri = "https://github.com/alibaba/dubbo/archive/dubbo-2.4.11.zip";
		uri = "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-windows.zip";
		HttpGet httpGet = new HttpGet(uri);

		httpGet.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");

		// String proxyHost = "proxy.wdf.sap.corp";
		// int proxyPort = 8080;
		// httpClient.getCredentialsProvider().setCredentials(
		// new AuthScope(proxyHost, proxyPort),
		// new UsernamePasswordCredentials("", ""));
		// HttpHost proxy = new HttpHost(proxyHost, proxyPort);
		// httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
		// proxy);

		httpClient.execute(httpGet, new ResponseHandler<File>() {

			String contentEnc = "";
			long contentLen = -1;
			String fileName = "";

			public File handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

					Header[] allHeaders = response.getAllHeaders();

					if (allHeaders.length > 0) {
						for (Header header : allHeaders) {
							String name = header.getName();
							System.out.println(name + " ==> "
									+ header.getValue());

							if ("Content-Disposition".equalsIgnoreCase(name
									.trim())) {
								String fileDescriptor = header.getValue();
								fileName = getFileName(fileDescriptor);
							}
						}
					}

					HttpEntity entity = response.getEntity();

					if (entity != null) {
						long contentLength = entity.getContentLength();
						Header contentType = entity.getContentType();
						System.out.println("contentType ==>"
								+ contentType.getValue());
						Header contentEncoding = entity.getContentEncoding();

						InputStream in = entity.getContent();

						File file = new File("src/test/resources", fileName
								.trim() == "" ? "default01.zip" : fileName);

						FileUtils.copyInputStreamToFile(in, file);

						System.out.println("下载完成....");

					}
				}

				return null;
			}
		}, context);

	}

	public static String getFileName(String headerValue) {
		String[] splits = headerValue.split(";");
		if (splits.length >= 2) {
			String name = splits[1].trim().split("=")[1].trim();
			System.out.println("filename ==> " + name);
			return name;
		}

		return "default.zip";
	}

	public static void main(String[] args) {
		String target = "Content-Disposition: attachment; filename=dubbo-dubbo-2.4.11.zip";
		String[] splits = target.split(";");
		if (splits.length >= 2) {
			String name = splits[1].trim().split("=")[1].trim();
			System.out.println(name);
		}
	}

	@Test
	public void testPut() throws Exception {
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(
				"https://xs01ca06b6163.ap1.hana.ondemand.com/jncpf2/wsb01.xsodata/WineSmallProduction('5BCA672AA43184D2B242BE2749B54B13')");
		httpPut.addHeader("Content-Type", "application/json;charset=UTF-8");
		HttpContext httpContext = new BasicHttpContext();
		
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("containerID", "45F72A9B1873CBAC87322769D594DAB7");
		jsonParam.put("boxID", "DFE74C483C4D9AB51CEC8AE1FBFACC9F");
		jsonParam.put("bottleEID", "448F881AECF7647331AEF66D996173BE");
		jsonParam.put("bottleIID", "3FF40C650FEA476DACB3ED3A9764929E");
		jsonParam.put("bottleVID", "EE410D11");
		jsonParam.put("status", 6);


		StringEntity entity = new StringEntity(jsonParam.toString(), "UTF-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");

		httpPut.setEntity(entity);

		HttpResponse response = httpClient.execute(httpPut, httpContext);
		
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		
		System.out.println(statusCode);
	}

}
