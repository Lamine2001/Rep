package com.siemens.windpower.fltp.scheduler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.common.ReadProperties;

public class FLTPDTTOutboundScheduler extends ScheduledThreadPoolExecutor {
	private int threadpoolsize;
	Logger logger = Logger.getLogger(FLTPDTTOutboundScheduler.class);

	public FLTPDTTOutboundScheduler(int threadpool) {
		super(threadpool);
		threadpoolsize = threadpool;
	}

	public void executeSchedulerTasks() throws IOException {
		 logger.info("In FLTPOutboundScheduler");

		ReadProperties read = new ReadProperties();
		Map prop = read.getPropertiesMap();
		Long timer = Long.parseLong(prop.get(DTTConstants.SCHEDULER_RUN_TIME)
				.toString());

		// logger.info("Scheduler will start in 60 Seconds");
		FLTPOutboundSchedule outboundschedule=new FLTPOutboundSchedule();
		outboundschedule.run();
		/*scheduleAtFixedRate(new FLTPOutboundSchedule(), 1, timer,
				TimeUnit.SECONDS);*/

	}

}
