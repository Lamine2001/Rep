package com.siemens.windpower.fltp.hanawsclient;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.siemens.windpower.fltp.p6wsclient.PrimaveraActivityWSClient;

public class TestClass {

	public static boolean isShorttermHorizonValid(Date oldDate, Date newDate,
			Integer shorttermhorizon) {
		// how to check if date1 is equal to date2
		boolean isshorttermhorizonvalid = false;
		System.out.println(oldDate);
		System.out.println(newDate);
		/*
		 * Long old =oldDate.getTime(); Long newd=newDate.getTime();
		 * shorttermhorizon=shorttermhorizon*86400000; System.out.println(old);
		 * System.out.println(newd); System.out.println((newd-old));
		 * System.out.println(shorttermhorizon);
		 * System.out.println((newd-old)/(86400000)); if ((newd-old) >=
		 * shorttermhorizon) {
		 * 
		 * isshorttermhorizonvalid=true; }
		 */
		int days = DifferenceInDays(oldDate, newDate);
		System.out.println(days);
		if (days >= shorttermhorizon) {

			isshorttermhorizonvalid = true;
		}

		return isshorttermhorizonvalid;
	}

	public final static long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;

	public static int DifferenceInDays(Date from, Date to) {
		return (int) ((to.getTime() - from.getTime()) / MILLISECONDS_IN_DAY);
	}

	public Date getSqldate(String datestr) throws ParseException {
		java.sql.Date sqlStartDate = null;

		if (datestr.equalsIgnoreCase("null") || datestr.equalsIgnoreCase("")) {
			return sqlStartDate;
		} else {

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			/* DateFormat df = new SimpleDateFormat(); */
			java.util.Date date = df.parse(datestr);
			sqlStartDate = new java.sql.Date(date.getTime());
		}

		return sqlStartDate;

	}

	/*
	 * public Date getSqldate(String datestr) throws ParseException{
	 * java.util.Date date=null; System.out.println("datestr "+datestr);
	 * if(datestr.equalsIgnoreCase("null")||datestr.equalsIgnoreCase("")){
	 * return date; } else{
	 * 
	 * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	 * 
	 * //DateFormat df = new SimpleDateFormat(); //date=df.parse(datestr); //
	 * System.out.println("Date format"+df.format(df.parse("2013-09-18"))); date
	 * = df.parse(datestr); //date=df.parse(df.format(date));
	 * System.out.println("date "+date);
	 * 
	 * 
	 * }
	 * 
	 * return date;
	 * 
	 * }
	 */
	private int round(double d) {
		double dAbs = Math.abs(d);
		int i = (int) dAbs;
		double result = dAbs - (double) i;
		if (result < 0.5) {
			return d < 0 ? -i : i + 1;
		} else {
			return d < 0 ? -(i + 1) : i + 1;
		}
	}

	public static void main(String[] args) throws Exception {
		TestClass activityclient = new TestClass();
		
		String start_dt = "2015-05-21";

		DateFormat parser = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = (Date) parser.parse(start_dt);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
        date = cal.getTime();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		System.out.println(formatter.format(date));

	}

}
