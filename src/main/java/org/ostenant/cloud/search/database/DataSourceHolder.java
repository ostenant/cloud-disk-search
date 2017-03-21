package org.ostenant.cloud.search.database;

/**
 * 项目名称：528e-sport <br>   
 * 类名称：DataSourceHolder <br>   
 * 类描述：  数据源存放key容器  <br>
 * 创建人：chenlin <br>  
 * 创建时间：2015-10-22 下午9:15:43 <br>  
 * 修改人：chenlin  <br>
 * 修改时间：2015-10-22 下午9:15:43   <br>
 * 修改备注：   <br>
 * @version
 */
public class DataSourceHolder {
	
	//线程本地环境  
	private static ThreadLocal<String> lookupKey = new ThreadLocal<String>(){
		protected String initialValue() {
			return MASTER_KEY;
		};
	};
	
	private final static String MASTER_KEY = "master";
	
	// private final static String SLAVE_KEY = "slave";
	
	/**
	 * @Title: getLookupKey <br>
	 * @Description: 获取数据源key  <br>
	 * @Author: chenlin <br>
	 * @param @return    设定文件 <br>
	 * @return String    返回类型   <br>
	 * @throws
	 */
	public static String getLookupKey(){
		return lookupKey.get();
	}
	
	/**
	 * @Title: setLookupKey <br>
	 * @Description: 设置数据源key  <br>
	 * @Author: chenlin <br>
	 * @param @param customType    设定文件 <br>
	 * @return void    返回类型   <br>
	 * @throws
	 */
	public static void setLookupKey(String customType){
		lookupKey.set(customType);
	}
	
	/**
	 * @Title: clearLookupKey <br>
	 * @Description: 移除数据源key <br>
	 * @Author: chenlin <br>
	 * @param     设定文件 <br>
	 * @return void    返回类型   <br>
	 * @throws
	 */
	public static void clearLookupKey(){
		lookupKey.remove();
	}

}
