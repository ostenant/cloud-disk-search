package org.ostenant.cloud.search.common;

public class BdsolaConfig {

	/**
	 * 爬虫数据库容器
	 */
	public static final String CRAWL_DATA_PLACE_HOLDER = "bdsola/crawldata";

	/**
	 * 爬虫一级存储目录
	 */
	public static final String FIRST_LEVEL_PLACE_HOLDER = "bdsola/sourcehtml/firstlevel";
	
	/**
	 * 检索关键字目录
	 */
	public static final String KEY_WORD_DIC_CONFIG = "bdsola/kword.dic.cfg";
	
	/**
	 * 爬虫进程数量
	 */
	public static final int CRAWL_THRAD_NUM = 20;
	
	
	/**
	 * 执行时间间隔
	 */
	public static final long EXECUTE_INTERVAL = 1000;
	
	/**
	 * 爬取层数
	 */
	public static final int CRAWL_GRADE = 1;
	
	/**
	 * 每一层爬取最大数
	 */
	public static final int CRAWL_TOP_N = 1;
	
	/**
	 * 开发模式
	 */
	public static final boolean RESUMABLE = false;
	

	/**
	 * 错误状态
	 */
	public static final String STATUS_ERROR = "ERROR";
	
	/**
	 * url id
	 */
	public static final String URL_LIST_IDS = "urlListIds";
	
	/**
	 * 字符编码
	 */
	public static final String CHARSET = "UTF-8";
	
	/**
	 * bdsolaConfigJSON
	 */
	public static final String BDSOLA_CONFIG_JSON = "bdsolaConfigJSON";
	
	/**
	 * bdsolaConfigMap
	 */
	public static final String BDSOLA_CONFIG_MAP = "bdsolaConfigMap";
	

	public static final class CrawlFileSystem {
		/**
		 * 文件存储系统选择
		 */
		private static final String LOCAL_FILE_SYSTEM = "local";

		/**
		 * 本地文件存储系统
		 */
		private static final String DISTRIBUTE_FILE_SYSTEM = "hdfs";

		/**
		 * 分布式文件系统
		 */
		public static String STORAGE_FILE_SYSTEM = LOCAL_FILE_SYSTEM;

		public static String hdfs() {
			STORAGE_FILE_SYSTEM = DISTRIBUTE_FILE_SYSTEM;
			return STORAGE_FILE_SYSTEM;
		}

		public static String local() {
			STORAGE_FILE_SYSTEM = LOCAL_FILE_SYSTEM;
			return STORAGE_FILE_SYSTEM;
		}

	}

	public static final class CrawlPlanner {
		/**
		 * 分布式抓取处理
		 */
		private static final String CRAWL_MAPREUDCE = "mapreduce";

		/**
		 * 单机抓取处理
		 */
		private static final String CRAWL_LOCAL = "single";

		/**
		 * 抓取方式
		 */
		public static String CRAWL_PLANNER = CRAWL_LOCAL;

		public static String mapreduce() {
			CRAWL_PLANNER = CRAWL_MAPREUDCE;
			return CRAWL_PLANNER;
		}

		public static String local() {
			CRAWL_PLANNER = CRAWL_LOCAL;
			return CRAWL_LOCAL;
		}

	}

}
