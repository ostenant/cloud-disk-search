package org.ellis.yun.search.test.webcollector.crawler;

import java.net.URLEncoder;
import java.util.List;

import org.ellis.yun.search.test.webcollector.utils.PageDriverUtils;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;

@SuppressWarnings("all")
public class DemoBingCrawler extends RamCrawler {

	public DemoBingCrawler(boolean autoParse) {
		super(autoParse);
		this.addRegex("http://yun.baidu.com/share/link*");
	}

	public void visit(Page page, CrawlDatums next) {
//		String html = page.getHtml();
//		System.out.println(html);
//		System.out
//				.println("------------------------------------------------------------------------------------");
	}

	public static void main(String[] args) throws Exception {

		DemoBingCrawler crawler = new DemoBingCrawler(true);
		crawler.setThreads(40);
		crawler.setTopN(40);
		crawler.addSeed(createBingUrl("http://pan.java1234.com/result.php?wp=0&op=0&ty=gn&q=hadoop"));
		crawler.start(2);

	}

	protected void handleByPhantomJsDriver(Page page) {
		// HtmlUnitDriver driver = PageDriverUtils.getHtmlUnitDriver(page);
		PhantomJSDriver driver = PageDriverUtils.getPhantomJSDriver(page);
		WebElement webElement = driver.findElement(By
				.tagName("button"));
		webElement.click();
		System.out.println(driver.getPageSource());
		driver.quit();
	}

	/**
	 * 根据关键词和页号拼接Bing搜索对应的URL
	 */
	public static String createBingUrl(String keyword) throws Exception {
		keyword = URLEncoder.encode(keyword, "utf-8");
		return String.format(
				"http://pan.java1234.com/result.php?wp=0&op=0&ty=gn&q=%s",
				keyword);
	}

}
