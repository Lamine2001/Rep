package com.siemens.windpower.fltp.scheduler;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.siemens.windpower.common.ReadProperties;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class FLTPDTTInboundScheduler extends ScheduledThreadPoolExecutor

{
	Logger logger = null;
	private int threadpoolsize;

	public FLTPDTTInboundScheduler(int threadpool) {
		super(threadpool);
		threadpoolsize = threadpool;
		logger = Logger.getLogger(FLTPDTTInboundScheduler.class);
	}

	public void executeSchedulerTasks() throws IOException {
		// logger.info("In FLTPInboundScheduler");
		ReadProperties read = new ReadProperties();
		Map prop = read.getPropertiesMap();
		Long timer = Long.parseLong(prop.get(DTTConstants.SCHEDULER_RUN_TIME)
				.toString());

		// logger.info("Scheduler will start in 60 Seconds");
		FLTPSchedule fltpschedule=new FLTPSchedule();
		fltpschedule.run();
		/*scheduleAtFixedRate(new FLTPSchedule(), 1, timer, TimeUnit.SECONDS);*/

	}

}
