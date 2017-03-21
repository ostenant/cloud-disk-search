package org.ellis.yun.search.test.webcollector.crawler;

import java.net.Proxy;

import org.jsoup.nodes.Document;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

/**
 * 
 * WebCollector 2.x版本的tutorial <br>
 * 2.x版本特性：<br>
 * 1）自定义遍历策略，可完成更为复杂的遍历业务，例如分页、AJAX <br>
 * 2）内置Berkeley DB管理URL，可以处理更大量级的网页<br>
 * 3）集成selenium，可以对javascript生成信息进行抽取<br>
 * 4）直接支持多代理随机切换<br>
 * 5）集成spring jdbc和mysql connection，方便数据持久化<br>
 * 6）集成json解析器 <br>
 * 7）使用slf4j作为日志门面<br>
 * 8）修改http请求接口，用户自定义http请求更加方便
 * 
 */
@SuppressWarnings("all")
public class ExtBreadthCrawler extends BreadthCrawler {

	private static final String PATH_CRAWL_DATA1 = "src/main/resources/bdsola/data1";

	/**
	 * 如果autoParse设置为true，遍历器会自动解析页面中符合正则的链接，加入后续爬取任务，否则不自动解析链接。
	 * 
	 * @param crawlPath
	 *            第一个参数是爬虫的crawlPath，crawlPath是维护URL信息的文件夹的路径，如果爬虫需要断点爬取，
	 *            每次请选择相同的crawlPath
	 * 
	 * @param autoParse
	 *            第二个参数表示是否自动抽取符合正则的链接并加入后续任务
	 */
	public ExtBreadthCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		/* BreadthCrawler可以直接添加URL正则规则 */
		this.addRegex("http://item.jd.com/.*.html");
	}

	public static void main(String[] args) throws Exception {

		ExtBreadthCrawler crawler = new ExtBreadthCrawler(PATH_CRAWL_DATA1,
				true);

		crawler.setThreads(50);
		crawler.addSeed("http://list.jd.com/list.html?cat=1319,1523,7052&page=1&go=0&JL=6_0_0");
		crawler.setResumable(false);
		
		// 多代理随机
		// RandomProxyGenerator proxyGenerator=new RandomProxyGenerator();
		// proxyGenerator.addProxy("127.0.0.1",8080,Proxy.Type.SOCKS);
		// requester.setProxyGenerator(proxyGenerator);

		/* 设置每层爬取爬取的最大URL数量 */
		crawler.setTopN(1000);

		/* 如果希望尽可能地爬取，这里可以设置一个很大的数，爬虫会在没有待爬取URL时自动停止 */
		crawler.start(1);

	}

	/**
	 * 用户自定义对每个页面的操作，一般将抽取、持久化等操作写在visit方法中。
	 * 
	 * @param page
	 * @param nextLinks
	 *            需要后续爬取的URL。如果autoParse为true，爬虫会自动抽取符合正则的链接并加入nextLinks。
	 */
	public void visit(Page page, CrawlDatums next) {
		Document doc = page.getDoc();
		String title = doc.title();
		System.out.println("URL:" + page.getUrl() + "  标题:" + title);
		System.out
				.println("==============================================================");
		System.out.println(page.getHtml());
		System.out
				.println("==============================================================");

		/*
		 * //添加到nextLinks的链接会在下一层或下x层被爬取，爬虫会自动对URL进行去重，所以用户在编写爬虫时完全不必考虑生成重复URL的问题
		 * //如果这里添加的链接已经被爬取过，则链接不会在后续任务中被爬取
		 * //如果需要强制添加已爬取过的链接，只能在爬虫启动（包括断点启动）时，通过Crawler.addForcedSeed强制加入URL。
		 * nextLinks.add("http://www.csdn.net");
		 */
	}

}
