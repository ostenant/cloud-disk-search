package org.ostenant.cloud.search.common.utils;

import org.apache.log4j.Logger;

public class PathParser {

	private static Logger logger = Logger.getLogger(PathParser.class);

	public static String parser(Class<?> clazz, String relativePath) {
		try {
			String realPath = clazz.getClassLoader().getResource(relativePath)
					.getPath().substring(1);
			logger.debug(" >> realPath: " + realPath);
			return realPath;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(" >> parser failed!");
			throw new RuntimeException("Fail...");
		}
		
	}
}
