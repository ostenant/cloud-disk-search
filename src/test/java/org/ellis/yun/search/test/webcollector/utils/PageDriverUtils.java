package org.ellis.yun.search.test.webcollector.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import cn.edu.hfut.dmic.webcollector.model.Page;

public class PageDriverUtils {

	private static final String PHANTOMJS_EXE_PATH = "F:/phantomjs-1.9.6-windows/phantomjs.exe";
	private static final String CASPERJS_EXE_PATH = "F:/phantomjs-1.9.6-casperjs-1.1.0-0/casperjs-casperjs-1.1.0-0/bin/casperjs.exe";

	/**
	 * 获取webcollector 自带htmlUnitDriver实例
	 * 
	 * @param page
	 * @param browserVersion
	 *            模拟浏览器
	 * @return
	 */
	public static HtmlUnitDriver getHtmlUnitDriver(Page page) {
		HtmlUnitDriver driver = new HtmlUnitDriver();
		driver.setJavascriptEnabled(true);
		driver.get(page.getUrl());
		return driver;
	}

	/**
	 * 获取webcollector <br>
	 * 自带htmlUnitDriver实例 选择浏览器版本<br>
	 * 
	 * @param page
	 * @param browserVersion
	 *            模拟浏览器
	 * @return
	 */
	public static HtmlUnitDriver getHtmlUnitDriver(Page page,
			BrowserVersion version) {
		HtmlUnitDriver driver = new HtmlUnitDriver(version);
		driver.setJavascriptEnabled(true);
		driver.get(page.getUrl());
		return driver;
	}

	/**
	 * 获取PhantomJsDriver(可以爬取js动态生成的html) <br>
	 * 通 过Selenium爬取数据
	 * 
	 * @param page
	 * @return
	 */
	public static PhantomJSDriver getPhantomJSDriver(Page page) {
		System.setProperty("phantomjs.binary.path", PHANTOMJS_EXE_PATH);
		PhantomJSDriver driver = new PhantomJSDriver();
		driver.executePhantomJS("phantomjs F:/phantomjs-1.9.6-casperjs-1.1.0-0/phantomjs-1.9.6-windows/demos/taobao.js");

		// WebDriver driver = new HtmlUnitDriver(true);

		// System.setProperty("webdriver.chrome.driver",
		// "D:\\Installs\\Develop\\crawling\\chromedriver.exe");
		// WebDriver driver = new ChromeDriver();
		// JavascriptExecutor js = (JavascriptExecutor) driver;
		// js.executeScript("function(){}");
		return driver;

	}

	/**
	 * 直接调用原生phantomJS(即不通过selenium)
	 * 
	 * @param page
	 * @return
	 */
	public static String getPhantomJS(Page page) {
		try {

			Runtime runtime = Runtime.getRuntime();
			Process process = runtime
					.exec(CASPERJS_EXE_PATH
							+ " F:/phantomjs-1.9.6-casperjs-1.1.0-0/casperjs-demos/yun.js");
			InputStream in = process.getInputStream();
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(reader);

			StringBuffer sb = new StringBuffer();
			String line = "";

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String[] args) {
		System.out.println(getPhantomJS(null));
	}

}
