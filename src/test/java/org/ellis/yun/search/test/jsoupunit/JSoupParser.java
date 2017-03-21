package org.ellis.yun.search.test.jsoupunit;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import java_cup.runtime.lr_parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.ostenant.cloud.search.bdsola.pojo.entity.BdSolaUrlItem;
import org.ostenant.cloud.search.common.LinkFetcher;
import org.ostenant.cloud.search.common.utils.CommonParser;
import org.ostenant.cloud.search.entity.LinkBean;
import org.ostenant.cloud.search.entity.LinkCollection;

@SuppressWarnings("all")
public class JSoupParser {

	private Logger logger = Logger.getLogger(JSoupParser.class);

	@Test
	public void testParseHTML() throws Exception {
		String html = CommonParser.html2String(new File(
				"src/test/resources/android_1.html"));
		Document doc = Jsoup.parse(html);
		treeView(doc.childNodes());
	}

	/**
	 * 测试Jsoup从一个URL加载一个html并解析
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoadFromUrl() throws Exception {
		String url = "http://www.bdsola.com/search.php?kw=spring&searchType=ALL&sort=0&cur_page=1";
		Connection connect = Jsoup.connect(url);
		Document doc = connect.get();

		Elements pages = doc.select(".page");
		System.out.println(pages.html());

		// 当前页
		Elements elements = doc.select(".page .current");
		String html = elements.html();
		int currentPage = Integer.parseInt(html);
		System.out.println(currentPage);

		// a标签最后一个children
		Elements aElements = doc.select(".page a");
		Element aLast = aElements.last();
		String href = aLast.attr("href");
		String lastPage = href.substring(href.lastIndexOf("=") + 1);

		int lastPageNum = Integer.parseInt(lastPage);
		System.out.println(lastPageNum);

	}

	@Test
	public void testAbs() throws Exception {
		Document doc = Jsoup.connect("http://www.open-open.com").get();

		Element link = doc.select("a").first();
		String relHref = link.attr("href"); // == "/"
		String absHref = link.attr("abs:href"); // "http://www.open-open.com/"
		System.out.println(relHref);
		System.out.println(absHref);
	}

	@Test
	public void testFetchAllLinks() throws Exception {
		String url = "http://www.bdsola.com/search.php?kw=spring&searchType=ALL&sort=0&cur_page=1";
		LinkCollection collection = LinkFetcher.fetchAllLinks(url,
				new LinkCollection(url));
		System.out
				.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(collection.getPageUrl());
		for (LinkBean linkBean : collection) {
			System.out.println(linkBean.getTagName() + " >> "
					+ linkBean.getLink());
		}
	}

	@Test
	public void testParseFirstLevel() throws Exception {
		String rootDir = "src/main/resources/bdsola/sourcehtml/firstlevel";
		String url = "/go.php?s_url=1kTu9Uq7";
		String yunId = url.substring(url.lastIndexOf("=") + 1);
		System.out.println(yunId);
	}

	@Test
	public void testUiParser() throws Exception {
		String sourcefirst = "C:/Users/i327119/Desktop/sourcefirst.html";
		String detail = "C:/Users/i327119/Desktop/detail.html";
		File cachedFile = new File(sourcefirst);
		if (cachedFile.exists() && cachedFile.isFile()) {
			Document doc = CommonParser.html2Document(cachedFile);
			Elements childrenDocs = doc.select("ul.flist");
			Element flistUlElem = null;
			ListIterator<Element> listIterator = childrenDocs.listIterator();
			while (listIterator.hasNext()) {
				flistUlElem = listIterator.next();
				int index = listIterator.nextIndex();
				logger.info("<<<<<<<<<<<<<<<<<<<<  FlistUlElem "//
						+ index//
						+ " >>>>>>>>>>>>>>>>>>>>>>> ");
				logger.info(flistUlElem.toString());
			}

			Elements lisElem = flistUlElem.select("li");
			ListIterator<Element> liElemIterator = lisElem.listIterator();

			List<BdSolaUrlItem> itemList = new ArrayList<BdSolaUrlItem>();

			for (int in = 0; liElemIterator.hasNext(); in++) {
				Element nextLiElem = liElemIterator.next();
				BdSolaUrlItem item = new BdSolaUrlItem();

				List<Node> childNodes = nextLiElem.childNodes();

				for (Node node : childNodes) {
					String nodeName = node.nodeName();
					item.setLastCrawlTime(new Date());
					if ("p".equalsIgnoreCase(nodeName)) { // fileType
						String fileType = node.attr("class");
						item.setFileType(fileType);
					} else if ("a".equalsIgnoreCase(nodeName)) { // href +
																	// title
						String title = node.attr("title");
						String href = node.attr("href");
						item.setHref(href);
						item.setTitle(title);
						item.setId(href.substring(3, href.lastIndexOf(".")));
					} else if ("span".equalsIgnoreCase(nodeName)) { // author
						List<Node> spanChildrenNodes = node.childNodes();
						Node spanANode = spanChildrenNodes.get(0);
						String author = spanANode.attr("title");
						item.setAuthor(author);
					} else if ("i".equalsIgnoreCase(nodeName)) { // time
						if (node.childNodeSize() > 0) {
							Node timeNode = node.childNode(0);
							item.setUploadTime(timeNode.toString());
						}
					}

				}
				// 缓存
				itemList.add(item);

				try {
					for (BdSolaUrlItem it : itemList) {
						String firstLevelUrl = BdSolaUrlItem.getDomain() + it.getHref();
						logger.debug("firstLevelUrl : " + firstLevelUrl);
						
//						Document secondaryDoc = Jsoup.connect(firstLevelUrl)
//								.get();
						File detailFile = new File(detail);
						Document secondaryDoc = CommonParser.html2Document(detailFile);
						if (secondaryDoc != null) {
							// f-ext
							Elements divSpanElem = secondaryDoc
									.select(".f-ext span");
							int spanSize = divSpanElem.size();
							if (spanSize > 0) {
								Element lastSpanElem = divSpanElem.last();

								// lastSpanElem.getAllElements() --
								// 获取当前元素下的所有子孙元素
								Elements as = lastSpanElem
										.getElementsByTag("a");
								if (as.size() > 0) {
									Element targetElem = as.first();
									// ** 获取元素 ** //
									String sendcondaryHref = targetElem
											.attr("href");
									if (StringUtils.isNotBlank(sendcondaryHref)) {
//										logger.info(" >> Current sencondary url: "
//												+ sendcondaryHref);
										it.setSecondary(sendcondaryHref); // /go.php?s_url=1kTu9Uq7
										String yunId = sendcondaryHref
												.substring(sendcondaryHref
														.lastIndexOf("=" + 1));
										it.setYunShareId(yunId);
										item.setLastCrawlTime(new Date());
									} else {
										logger.warn(" >> Failed to parse sencondary url");
									}
								}
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				for (BdSolaUrlItem it : itemList) {
					System.out.println(it);
				}

			}
		}
	}

	private void treeViewFirstLevel(File root) {
		if (root.exists()) {
			if (root.isDirectory()) {
				File[] files = root.listFiles();
			} else {
				doOperationParse(root);
			}

		}
	}

	private void doOperationParse(File root) {
	}

	private void treeView(List<Node> childNodes) {
		String blank = "";
		for (Node node : childNodes) {
			if (node.childNodes().size() > 0) {
				treeView(node.childNodes());
			} else {
				blank += " ";
				System.out.println(blank + node.nodeName());
			}
		}
	}

}
