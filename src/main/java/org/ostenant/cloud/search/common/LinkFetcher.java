package org.ostenant.cloud.search.common;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ostenant.cloud.search.entity.LinkBean;
import org.ostenant.cloud.search.entity.LinkCollection;

public class LinkFetcher {

	public static LinkCollection fetchAllLinks(String url,
			LinkCollection linkCollection) throws IOException {
		Validate.isTrue(StringUtils.isNotEmpty(url),
				"usage: supply url to fetch");
		print("Fetching %s...", url);

		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");

		print("\nMedia: (%d)", media.size());
		for (Element src : media) {
			if (src.tagName().equals("img")) {
				print(" * %s: <%s> %sx%s (%s)", src.tagName(),
						src.attr("abs:src"), src.attr("width"),
						src.attr("height"), trim(src.attr("alt"), 20));
				if (StringUtils.isNoneBlank(src.attr("abs:src"))) {
					linkCollection.add(new LinkBean(src.tagName(), src
							.attr("abs:src")));
				}
			} else {
				print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
				if (StringUtils.isNoneBlank(src.attr("abs:src"))) {
					linkCollection.add(new LinkBean(src.tagName(), src
							.attr("abs:src")));
				}
			}
		}

		print("\nImports: (%d)", imports.size());
		for (Element link : imports) {
			print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"),
					link.attr("rel"));
			if (StringUtils.isNoneBlank(link.attr("abs:href"))) {
				linkCollection.add(new LinkBean(link.tagName(), link
						.attr("abs:href")));
			}
		}

		print("\nLinks: (%d)", links.size());
		for (Element link : links) {
			print(" * a: <%s>  (%s)", link.attr("abs:href"),
					trim(link.text(), 35));
			if (StringUtils.isNoneBlank(link.attr("abs:href"))) {
				linkCollection.add(new LinkBean(link.tagName(), link
						.attr("abs:href")));
			}
		}

		return linkCollection;
	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}
}
