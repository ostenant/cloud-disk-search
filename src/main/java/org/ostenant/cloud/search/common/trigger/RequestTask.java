package org.ostenant.cloud.search.common.trigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public abstract class RequestTask {

	protected static Logger logger = null;
	protected SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss SSS");
	
	protected Date lastTriggerTime;
	protected long totalTimes;
	
	public Date getLastTriggerTime() {
		return lastTriggerTime;
	}

	public void setLastTriggerTime(Date lastTriggerTime) {
		this.lastTriggerTime = lastTriggerTime;
	}

	public long getTotalTimes() {
		return totalTimes;
	}

	public void setTotalTimes(long totalTimes) {
		this.totalTimes = totalTimes;
	}

	public RequestTask() {
		logger = Logger.getLogger(this.getClass());
	}
	
	public abstract void handleTrigger();

	public void execute() {
		logger.info("\nExecute start time is " + format.format(new Date()));
		handleTrigger();
		logger.info("Execute end time is " + format.format(new Date()));
	}

}
