package org.ellis.yun.search.test.httpclient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.remote.http.HttpMethod;

/**
 * @ClassName: CrawlHuxiu
 * @Description: 
 *               jdk6下采用httpclient3.1、线程池ThreadPoolExecutor、jsoup1.7.1和commons.io
 *               等工具包多线程抓取虎嗅网全部文章，并以文本文件形式持久化入磁盘
 */
@SuppressWarnings("all")
public class CrawlHuxiu {

	/**
	 * @Title: main
	 * @Description: 采用多线程抓取虎嗅网全文
	 * @param args
	 * @author
	 * @throws IOException
	 * @date 2012-12-15
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {

		CrawlHuxiu crawlHuxiu = new CrawlHuxiu();

		// 创建 HttpClient 的多线程实例
		ClientConnectionManager connectionManager = new PoolingClientConnectionManager();
		HttpClient httpClient = new DefaultHttpClient(connectionManager);
		crawlHuxiu.getHuxiu(httpClient,
				"http://www.huxiu.com/article/8000/1.html");
	}

	/**
	 * 根据jsoup解析出爬取回来的虎嗅网文章内容，利用common.io包放入磁盘
	 * 
	 * @param doc
	 * @param i
	 * @throws IOException
	 */
	static void crawlHuxiu(Document doc, int i) throws IOException {
		List<String> yuliao = new ArrayList<String>();
		Elements title = doc.select("title");// 主题
		String str = title.first().text().replace("-看点-@虎嗅网", "")
				.replace("-观点-@虎嗅网", "").replace("-读点-@虎嗅网", "");
		System.out.println("title： " + str);
		Elements userAndTime = doc.select(".author-name"); // 发帖时间
		Elements content = doc.select("#article_content");// 内容
		if (str.contains("提示信息 - 虎嗅网")) {
			System.out.println("文章被删除");
			return;
		}
		String baseUrl = "http://www.huxiu.com/article/";
		yuliao.add(str);
		yuliao.add(baseUrl + i + "/1.html");
		yuliao.add(userAndTime.get(0).select(".fc1").text());
		yuliao.add(userAndTime.get(1).text());
		yuliao.add(content.get(0).text());
		File file = new File("c:\\huxiu\\" + str + ".txt");
		FileUtils.writeLines(file, yuliao);
		yuliao.clear();
		return;
	}

	/**
	 * 线程池执行的任务，抓取网页
	 */
	static class ThreadPoolTask implements Runnable {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		int i = 0;
		// 保存任务所需要的数据
		private Object threadPoolTaskData = null;

		ThreadPoolTask(Object tasks) {
			this.threadPoolTaskData = tasks;
		}

		public ThreadPoolTask(HttpMethod getMethod, int i) {
		}

		public ThreadPoolTask(HttpClient httpClient, HttpGet httpGet, int i) {
			this.httpClient = httpClient;
			this.httpGet = httpGet;
			this.i = i;
		}

		public Object getThreadPoolTaskData() {
			return threadPoolTaskData;
		}

		public void setThreadPoolTaskData(Object threadPoolTaskData) {
			this.threadPoolTaskData = threadPoolTaskData;
		}

		public void run() {
			// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
			try {

				System.out.println("executing request " + httpGet.getURI());

				// 执行getMethod
				HttpResponse response = httpClient.execute(httpGet);
				int status = response.getStatusLine().getStatusCode();
				System.out.println("status:" + status);

				// 连接返回的状态码
				if (HttpStatus.SC_OK == status) {

					// 获取到的内容
					Document doc = null;
					try {
						doc = Jsoup.parse(response.getEntity().getContent(),
								"utf-8", "");
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (httpGet.getURI().toString()
							.startsWith("http://www.huxiu.com/article/")) {
						crawlHuxiu(doc, i);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 释放连接
				httpGet.releaseConnection();
			}
		}
	}

	/**
	 * 获取虎嗅网的全体文章
	 * 
	 * @param httpClient
	 * @param startUrl
	 * @throws InterruptedException
	 */
	public void getHuxiu(HttpClient httpClient, String startUrl)
			throws InterruptedException {
		// 构造一个线程池
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(100, 500, 3,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10000),
				new ThreadPoolExecutor.DiscardOldestPolicy());
		String baseUrl = "http://www.huxiu.com/article/";
		// 请求URI
		HttpGet getMethod = null;

		String id = startUrl.split("/")[4];
		Integer count = Integer.valueOf(id);
		for (int i = count; i > 100; i--) {
			// 根据虎嗅网文章url的特点，构造请求URI，用具有100个活动线程的线程池进行加载
			getMethod = new HttpGet(baseUrl + i + "/1.html");
			threadPool.execute(new ThreadPoolTask(httpClient, getMethod, i));
			Thread.sleep(10);
		}
		while (true) {
			Thread.sleep(10);
			if (threadPool.getActiveCount() == 0) {
				threadPool.shutdown();
				break;
			}
		}
	}
}
