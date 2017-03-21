package org.ostenant.cloud.search.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CommonParser {

	public static Document html2Document(String html) {
		return Jsoup.parse(html);
	}

	/**
	 * 从文件中读取html生成字符串
	 * @param html
	 * @return
	 */
	public static String html2String(File html) {
		StringBuffer sb = new StringBuffer();
		String tmp = "";
		if (html.getAbsolutePath().endsWith(".html")) {
			BufferedReader br = null;
			InputStream in = null;
			InputStreamReader reader = null;
			try {
				in = new FileInputStream(html);
				reader = new InputStreamReader(in, "UTF-8");
				br = new BufferedReader(reader);
				while ((tmp = br.readLine()) != null) {
					sb.append(tmp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						br = null;
					}
				}
			}
		} else {
			return null;
		}

		String sourceHtml = sb.toString();
		return sourceHtml;
	}

	/**
	 * 从文件中读取html生成DOM
	 * @param html
	 * @return
	 */
	public static Document html2Document(File html) {
		StringBuffer sb = new StringBuffer();
		String tmp = "";
		if (html.getAbsolutePath().endsWith(".html")) {
			BufferedReader br = null;
			InputStream in = null;
			InputStreamReader reader = null;
			try {
				in = new FileInputStream(html);
				reader = new InputStreamReader(in, "UTF-8");
				br = new BufferedReader(reader);
				while ((tmp = br.readLine()) != null) {
					sb.append(tmp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						br = null;
					}
				}
			}
		} else {
			return null;
		}

		String sourceHtml = sb.toString();
		return html2Document(sourceHtml);
	}

}
