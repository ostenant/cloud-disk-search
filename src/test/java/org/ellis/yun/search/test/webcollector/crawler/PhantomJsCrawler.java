package org.ellis.yun.search.test.webcollector.crawler;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.ellis.yun.search.test.webcollector.utils.PageDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class PhantomJsCrawler extends BreadthCrawler {

	private static final String PATH_CRAWL_DATA1 = "D://crawl//data1";

	public PhantomJsCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		// BreadthCrawler可以直接添加URL正则规则 
		this.addRegex("http://pan.baidu.com/.*.html");
	}

	protected void handleByPhantomJsDriver(Page page) {
		// HtmlUnitDriver driver = PageDriverUtils.getHtmlUnitDriver(page);
		PhantomJSDriver driver = PageDriverUtils.getPhantomJSDriver(page);
		List<WebElement> divInfos = driver.findElements(By
				.cssSelector("li.gl-item"));
		System.out.println(driver.getPageSource());
		for (WebElement divInfo : divInfos) {
			WebElement price = divInfo.findElement(By.className("J_price"));
			System.out.println(price + ":" + price.getText());
		}
		driver.quit();
	}

	public static void main(String[] args) throws Exception {
		PhantomJsCrawler crawler = new PhantomJsCrawler(PATH_CRAWL_DATA1, true);

		FileUtils.deleteDirectory(new File(PATH_CRAWL_DATA1));

		crawler.setResumable(true);
		crawler.setThreads(20);
		crawler.addSeed("http://pan.baidu.com/");
		crawler.start(1);
	}


	public void visit(Page page, CrawlDatums next) {
		//handleByPhantomJsDriver(page);
		System.out.println(page.getHtml());
	}

}
