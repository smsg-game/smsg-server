package com.lodogame.game.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 日期工具集合
 * 
 * @author CJ
 * 
 */
public class DateUtils {

	public static String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return format.format(date);
	}

	public static String getDateForLog() {
		return getDateForLog(new Date());
	}

	public static String getDateForLog(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}

	public static String getDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	public static String getDateStr(final long millis, String fmt) {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Date date = new Date();
		if (millis > 0) {
			date.setTime(millis);
		}

		return format.format(date);
	}

	public static Date getDateAtMidnight() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getDateAtMidnight(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static Date getDateAtMidnight(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(date +" "+ "00:00:00");
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取两天相差天数
	 * 
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static int getDayDiff(Date dt1, Date dt2) {
		long t1 = dt1.getTime();
		long t2 = dt2.getTime();
		double diff = t2 - t1;
		diff = diff / (24 * 60 * 60 * 1000);
		return (int) diff;
	}
	
	public static int getMinuteDiff(Date dt1, Date dt2) {
		long t1 = dt1.getTime();
		long t2 = dt2.getTime();
		double diff = t2 - t1;
		diff = diff / (1000 * 60);
		return (int) diff;
		
	}
	
	public static int getSecondDiff(Date dt1, Date dt2) {
		long t1 = dt1.getTime();
		long t2 = dt2.getTime();
		double diff = t2 - t1;
		diff = diff / 1000;
		
		return (int) diff;
	}

	public static int getYear() {
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.YEAR);
	}

	public static int getHour() {
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.HOUR_OF_DAY);
	}

	public static int getDay() {
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.DAY_OF_MONTH);
	}

	public static int getMonth() {
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.MONTH);
	}

	public static int getMinute() {
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.MINUTE);
	}

	public static int getDayOfWeek() {
		Calendar cld = Calendar.getInstance();
		cld.add(Calendar.DATE, -1);
		return cld.get(Calendar.DAY_OF_WEEK);
	}

	public static int getDayOfWeek(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		cld.add(Calendar.DATE, -1);
		return cld.get(Calendar.DAY_OF_WEEK);
	}

	public static int getWeekOfYear() {
		Calendar cld = Calendar.getInstance();
		cld.setFirstDayOfWeek(Calendar.MONDAY);
		return cld.get(Calendar.WEEK_OF_YEAR);
	}

	public static String getTime() {
		long time = System.currentTimeMillis();
		return getTime(time);
	}

	public static String getTime(final long millis) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		if (millis > 0) {
			date.setTime(millis);
		}
		return format.format(date);
	}

	public static String getTimeStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static Date addDays(Date date, int amount) {
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}

	/**
	 * 
	 * @param calendarField
	 *            amount 的类型，例如 <code>Calendar.MILLISECOND</code>
	 * @param amount
	 * @return 增加了 amount 的时间后得到的新的时间
	 */
	public static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	public static Date str2Date(String s) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date getYesterday(Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.DATE, -1);
		return cl.getTime();
	}

	public static boolean isSameDay(Date date1, Date date2) {
		return org.apache.commons.lang.time.DateUtils.isSameDay(date1, date2);
	}

	/**
	 * 获取多少秒之后的时间
	 * 
	 * @param createdTime
	 * @param second
	 */
	public static Date getAfterTime(Date date, int second) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.SECOND, second);
		return cl.getTime();
	}

	/**
	 * 根据星期和时间（小时和分钟）获取 long 型的时间表示。假设今天是2014-01-14，如果要获取这个礼拜星期四上午10:30的时间， 则输入：
	 * <li>"4 10:30" 可以获得 2014-01-16 10:30:00 的时间的 long 型表示
	 * 
	 * @return
	 */
	public static long getTimeByDayOfWeek(String str) {
		String[] timeStr = str.split(" ");

		int dayOfWeek = Integer.valueOf(timeStr[0]);

		String[] time = timeStr[1].split(":");

		int hour = Integer.valueOf(time[0]);
		int minute = Integer.valueOf(time[1]);
		int second = 0;

		if (time.length == 3) {
			second = Integer.valueOf(time[2]);
		}

		Calendar cl = Calendar.getInstance();
		cl.setFirstDayOfWeek(Calendar.MONDAY);
		cl.set(Calendar.DAY_OF_WEEK, dayOfWeek + 1);
		cl.set(Calendar.HOUR_OF_DAY, hour);
		cl.set(Calendar.MINUTE, minute);
		cl.set(Calendar.SECOND, second);

		if(dayOfWeek>7){
			cl.add(Calendar.DATE, 7);
		}
		
		return cl.getTimeInMillis();
	}

	public static boolean isBetween(Date date, Date startDate, Date endDate) {
		if (date.after(startDate) && date.before(endDate)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(date);
	}
	
	/**
	 * 
	 * @param time ("HH:mm:ss")
	 * @return
	 */
	public static Date getTimeDate(String time){
		String date = getTime();
		String[] str = date.split(" ");
		
		str[1] = time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(str[0]+" "+str[1]);
		} catch (ParseException e) {
			return null;
		}
	}

	public static void main(String[] args) {
		String nowStr = getDate();
		Date now = new Date();
		Date addDays = addDays(now, 2);
		String future = getDate(addDays);
		System.out.println(nowStr + " " + future);
		
	}
}
