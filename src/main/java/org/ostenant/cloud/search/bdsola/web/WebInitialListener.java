package org.ostenant.cloud.search.bdsola.web;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.ostenant.cloud.search.common.BdsolaConfig;
import org.ostenant.cloud.search.common.JSONFactory;
import org.ostenant.cloud.search.common.utils.PathParser;

public class WebInitialListener implements ServletContextListener {

	private static Logger logger = Logger.getLogger(WebInitialListener.class);
	private static final String DEFAULT_PATH = "bdsola/bdsolacfg.json";


	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info(" >> WebInitialListener Initialized");
		ServletContext sc = sce.getServletContext();
		String realPath = PathParser.parser(WebInitialListener.class,
				DEFAULT_PATH);
		JSONObject jsonObj = JSONFactory.readFileToJSON(realPath);
		HashMap<String, Object> map = JSONFactory.convertJSONToMap(realPath);
		sc.setAttribute(BdsolaConfig.BDSOLA_CONFIG_JSON, jsonObj);
		logger.info(" >> put bdsolaConfigJSON into ServletContext >> "
				+ jsonObj.toString());
		sc.setAttribute("bdsolaConfigMap", map);
		logger.info(" >> put bdsolaConfigMap into ServletContext >> "
				+ map.size());

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info(" >> WebInitialListener Destroyed");
	}

}
