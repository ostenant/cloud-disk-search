package org.ostenant.cloud.search.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class JSONFactory {

	private static Logger logger = Logger.getLogger(JSONFactory.class);

	public static JSONObject readFileToJSON(String realpath) {
		File file = new File(realpath);
		if (file.exists() && file.isFile()) {
			FileInputStream fis = null;
			StringBuffer buffer = new StringBuffer();
			try {
				fis = new FileInputStream(file);
				List<String> lines = IOUtils.readLines(fis, "UTF-8");
				for (String line : lines) {
					buffer.append(line);
				}
				String JSONStr = buffer.toString();
				logger.debug(" >> JSONStr: " + JSONStr);
				// 将json字符串转换成jsonObject
				JSONObject jsonObject = new JSONObject(JSONStr);

				return jsonObject;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					IOUtils.closeQuietly(fis);
				}
			}
		}
		return new JSONObject();
	}

	public static HashMap<String, Object> convertJSONToMap(String realpath) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		File file = new File(realpath);
		if (file.exists() && file.isFile()) {
			FileInputStream fis = null;
			StringBuffer buffer = new StringBuffer();
			try {
				fis = new FileInputStream(file);
				List<String> lines = IOUtils.readLines(fis, "UTF-8");
				for (String line : lines) {
					buffer.append(line);
				}
				String JSONStr = buffer.toString();
				// 将json字符串转换成jsonObject
				JSONObject jsonObject = new JSONObject(JSONStr);
				@SuppressWarnings("rawtypes")
				Iterator it = jsonObject.keys();
				while (it.hasNext()) {
					String key = String.valueOf(it.next());
					Object value = jsonObject.get(key);
					map.put(key, value);
					logger.debug(" >> key: " + key + " >> value: " + value);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					IOUtils.closeQuietly(fis);
				}
			}
		}
		return map;
	}

}
