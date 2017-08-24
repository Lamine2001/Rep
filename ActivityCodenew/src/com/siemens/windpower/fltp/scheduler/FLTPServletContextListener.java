package com.siemens.windpower.fltp.scheduler;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.siemens.windpower.common.DTTConstants;
import com.siemens.windpower.common.ReadProperties;
import com.siemens.windpower.fltp.p6wsclient.PrimaveraWSClient;
import com.siemens.windpower.fltp.scheduler.FLTPDTTInboundScheduler;
import com.siemens.windpower.fltp.scheduler.FLTPDTTOutboundScheduler;
import com.siemens.windpower.common.DTTErrorConstants;
import com.siemens.windpower.common.DTTConstants;

public class FLTPServletContextListener implements ServletContextListener {
	String multiregion = null;
	private FLTPDTTInboundScheduler inboundScheduler = null;
	private FLTPDTTOutboundScheduler outboundScheduler = null;
	Logger logger = Logger.getLogger(FLTPServletContextListener.class);

	public void contextInitialized(ServletContextEvent sce) {

		try {

			ReadProperties properties = new ReadProperties();

			Map propmap;

			propmap = properties.getPropertiesMap();
			multiregion = propmap.get(DTTConstants.IS_MULTI_REGION_ENABLED)
					.toString();
			// logger.info("FLTPServletContextListener :CONTEXT INTIALIZED");
			inboundScheduler = new FLTPDTTInboundScheduler(
					DTTConstants.MAXIMUM_TASKS);
			logger.info("FLTPServletContextListener :Inbound Scheduler About To Start");
			logger.info("FLTPServletContextListener :Inbound Scheduler Started");
			inboundScheduler.executeSchedulerTasks();
			

			
			  if(multiregion.equals(DTTConstants.Y)) {
			  logger.info("Multiregion Enabled"+multiregion); 
			  logger.info("FLTPServletContextListener :Outbound Scheduler About To Start");
			  outboundScheduler = new FLTPDTTOutboundScheduler(
			  DTTConstants.MAXIMUM_TASKS);
			  outboundScheduler.executeSchedulerTasks(); 
			  logger.info("FLTPServletContextListener :Outbound Scheduler End"); 
			  }
			 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// //logger.info(DTTErrorConstants.ERR045);
			logger.error(DTTErrorConstants.ERR045, e);

		}

	}

	public void contextDestroyed(ServletContextEvent sce) {

		inboundScheduler.shutdown();
		if (multiregion.equals("Y")) {
			outboundScheduler.shutdown();
		}

		// logger.info("FLTPServletContextListener :CONTEXT DESTROYED");
	}

}
