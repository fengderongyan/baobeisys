package com.sgy.util.common;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DateHelper
{
	private static String defaultPattern = "yyyy-MM-dd";

	/**
	 * 得到今天的日期 默认格式：yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getToday()
	{
		return getToday(defaultPattern);
	}

	/**
	 * 得到今天的日期
	 * 
	 * @param pattern
	 *            格式：如yy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getToday(String pattern)
	{
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * 得到昨天的日期 默认格式：yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getYesterday()
	{
		return getYesterday(defaultPattern);
	}

	public static String getDay(int days)
	{
		return getDay(days, defaultPattern);
	}

	public static String getDay(int days, String pattern)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(cal.getTime());
	}

	/**
	 * 得到昨天的日期
	 * 
	 * @param pattern
	 *            格式：如yy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getYesterday(String pattern)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(cal.getTime());
	}

	/**
	 * 得到月份列表 eg: getMonthList(-5,5) 取前5个月与后5个月
	 * 
	 * @param beforOffset
	 *            当月之前的偏移量(负数)
	 * @param afterOffset
	 *            当月之后的偏移量(正数)
	 * @return
	 */
	public static List<Map<String, String>> getMonthList(int beforOffset,
	        int afterOffset)
	{
		return getMonthList(beforOffset, afterOffset, "yyyy-MM");
	}
	
	/**
	 * 得到月份列表 eg: getMonthList("2008-01","2007-08","yyyy-MM")
	 * 
	 * @param startMonth
	 *            开始月份
	 * @param endMonth
	 *            结束月份
	 * @param pattern
	 *            格式：如yyyy-MM
	 * @return
	 */
	public static List<Map<String, String>> getDayList(String startday,
	        String endday, String pattern)
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try
		{
			Date startDate = format.parse(startday);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endday);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			if(startCal.compareTo(endCal) <= 0) {
				while (startCal.compareTo(endCal) <= 0)
				{
					Hashtable<String, String> hash = new Hashtable<String, String>();
					String month = format.format(startCal.getTime());
					hash.put("DAY", month);
					list.add(hash);
					startCal.add(Calendar.DAY_OF_MONTH, 1);
				}
			} else {
				while (startCal.compareTo(endCal) >= 0)
				{
					Hashtable<String, String> hash = new Hashtable<String, String>();
					String month = format.format(startCal.getTime());
					hash.put("DAY", month);
					list.add(hash);
					startCal.add(Calendar.DAY_OF_MONTH, -1);
				}
			}
		}
		catch (ParseException e)
		{
		}
		return list;
	}


	/**
	 * 得到月份列表 eg: getMonthList(-5,5,"yyyy-MM") 取前5个月与后5个月
	 * 
	 * @param beforOffset
	 *            前偏移量
	 * @param afterOffset
	 *            后偏移量
	 * @param pattern
	 *            格式：如yyyy-MM
	 * @return
	 */
	public static List<Map<String, String>> getMonthList(int beforOffset,
	        int afterOffset, String pattern)
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, beforOffset);
		int offset = afterOffset - beforOffset;
		if (offset >= 0)
		{
			for (int i = 0; i <= offset; i++)
			{
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String month = format.format(cal.getTime());
				hash.put("MONTH", month);
				list.add(hash);
				cal.add(Calendar.MONTH, 1);
			}
		}
		else
		{
			for (int i = 0; i >= offset; i--)
			{
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String month = format.format(cal.getTime());
				hash.put("MONTH", month);
				list.add(hash);
				cal.add(Calendar.MONTH, -1);
			}
		}
		return list;
	}

	/**
	 * 得到月份列表 eg: getMonthList("2008-01","2007-08","yyyy-MM")
	 * 
	 * @param startMonth
	 *            开始月份
	 * @param endMonth
	 *            结束月份
	 * @param pattern
	 *            格式：如yyyy-MM
	 * @return
	 */
	public static List<Map<String, String>> getMonthList(String startMonth,
	        String endMonth, String pattern)
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try
		{
			Date startDate = format.parse(startMonth);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endMonth);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			while (startCal.compareTo(endCal) <= 0)
			{
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String month = format.format(startCal.getTime());
				hash.put("MONTH", month);
				list.add(hash);
				startCal.add(Calendar.MONTH, 1);
			}
		}
		catch (ParseException e)
		{
		}
		return list;
	}

	/**
	 * 得到指定月份
	 * 
	 * @param offset
	 *            偏移量 offset = -1 取得上一个月 offset = 0 取得本月 offset = 1 取得下一个月
	 * @param pattern
	 *            模式
	 * @return
	 * 
	 */
	public static String getMonth(int offset, String pattern)
	{
		String yearMonth = "197101";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.MONTH, offset);
		yearMonth = format.format(nowCal.getTime());
		return yearMonth;
	}
	
	public static String getMonth(int offset, String pattern,String currentMonth)
	{
		String yearMonth = "197101";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try
		{
			Date startDate = format.parse(currentMonth);
			Calendar nowCal = Calendar.getInstance();
			nowCal.setTime(startDate);
			nowCal.add(Calendar.MONTH, offset);
			yearMonth = format.format(nowCal.getTime());
		}
		catch (ParseException e)
		{
			System.out.println("d");
		}
		return yearMonth;
	}
	
	/**
	 * 得到上月同一天，如果当天为月底的话，上月也取月底，否则取同一日
	 * @param offset
	 * @param pattern
	 * @param currentMonth
	 * @return
	 */
	public static String getMonthDay(int offset, String pattern, String currentMonth)
	{
		String yearMonth = "197101";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		SimpleDateFormat formatMM = new SimpleDateFormat("MM");
		try
		{
			Date startDate = format.parse(currentMonth);
			Calendar nowCal = Calendar.getInstance();
			nowCal.setTime(startDate);
			Calendar tomorrowCal = Calendar.getInstance();
			tomorrowCal.setTime(startDate);
			tomorrowCal.add(Calendar.DAY_OF_MONTH, 1);
			if(tomorrowCal.get(Calendar.MONTH) != nowCal.get(Calendar.MONTH)) {
				tomorrowCal.add(Calendar.MONTH, offset);
				tomorrowCal.add(Calendar.DAY_OF_MONTH, -1);
				yearMonth = format.format(tomorrowCal.getTime());
			} else {
				nowCal.add(Calendar.MONTH, offset);
				yearMonth = format.format(nowCal.getTime());
			}
		}
		catch (ParseException e)
		{
			System.out.println("d");
		}
		return yearMonth;
	}

	/**
	 * 得到一周第几天
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date)
	{
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		aCalendar.setTime(date);
		int x = aCalendar.get(Calendar.DAY_OF_WEEK)-1;
		if(x==0)
			x=7;
		return x;
	}
	
	/**
	 * 得到一月第几天
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date){
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		aCalendar.setTime(date);
		int x=aCalendar.get(Calendar.DAY_OF_MONTH);
		return x;
	}
	
	public static int getLastDayOfMonth(){
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		int lastday=aCalendar.getActualMaximum(Calendar.DATE);
		return lastday;
	}
	

	public static Date getNextWeekFirstDate(Date date)
	{
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		int dayOfWeek=0;
		
		for(int i=1;i<=7;i++)
		{
			c.add(Calendar.DAY_OF_MONTH, 1);
			dayOfWeek=c.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek==2)
				return c.getTime();
			 
		}
		return date;
		
	}
	
	public static String getNextWeekFirstDay(Date date)
	{
		
		DateFormat format=  new SimpleDateFormat("yyyy-MM-dd");
		
		return format.format(getNextWeekFirstDate(date));
		
	}

	public static Date getNextWeekLastDate(Date date)
	{
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(getNextWeekFirstDate(date));
		int dayOfWeek=0;
		for(int i=1;i<=7;i++)
		{
			c.add(Calendar.DAY_OF_MONTH, 1);
			dayOfWeek=c.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek==1)
				return c.getTime();
			 
		}
		return date;
	} 
	
	public static String getNextWeekLastDay(Date date)
	{
		
		DateFormat format=  new SimpleDateFormat("yyyy-MM-dd");
		
		return format.format(getNextWeekLastDate(date));
		
	}
	
	
	
	
	
	public static String  getFirstDayOfMonth(Date date)
	{
		DateFormat format=  new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(c.getTime());
		
	}
	
	public static Date  getFirstDateOfMonth(Date date)
	{
		DateFormat format=  new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
		
	}
	
	
	
	/**
	 * 得到两个日期的相差的天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDaysDiff(Date date1,Date date2)
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return (int) Math.abs((c1.getTimeInMillis()-c2.getTimeInMillis()) / (24 * 3600 * 1000));

	}
	
	public static int getMinutesDiff(Date date1,Date date2)
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return (int) (c1.getTimeInMillis()-c2.getTimeInMillis()) / (60 * 1000);

	}

	/**
	 * 得到传入日期的周次（在一个月内）
	 * 
	 * @return
	 */
	public static int getWeekOfMonth(Date date)
	{
		DateFormat format=  new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Date firstDateOfMonth=getFirstDateOfMonth(date);
		int dayOfWeek=getDayOfWeek(firstDateOfMonth);
	
		Date fisrtMondayOfMonth=null;
		//如果是周一
		if(dayOfWeek==1)
		{
			fisrtMondayOfMonth=firstDateOfMonth;
		}
		else
		{
			Calendar c1 = Calendar.getInstance();
			c1.setTime(date);
			//c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			dayOfWeek=c1.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek==1)
				c1.add(Calendar.DAY_OF_MONTH,-7);
			else
				c1.add(Calendar.DAY_OF_MONTH,-(c1.get(Calendar.DAY_OF_WEEK)-2));
			
			Date firstDateOfLastMonth=getFirstDateOfMonth(c1.getTime());
			
			fisrtMondayOfMonth=getFirstMondayOfMonth(firstDateOfLastMonth);
		}
		int diff=getDaysDiff(date,fisrtMondayOfMonth);
		int weekOfMonth=diff/7+1;
		//return aCalendar.get(Calendar.WEEK_OF_MONTH);
		
		 //  return.format(c.getTime());
		   return weekOfMonth;
	}


	public static Date getFirstMondayOfMonth(Date date)
	{
		Date firstDate=getFirstDateOfMonth(date);
		Calendar c = Calendar.getInstance();
		c.setTime(firstDate);
		int dayOfWeek=0;
		for(int i=0;i<30;i++)
		{
		  
			dayOfWeek=c.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek==2)
				return  c.getTime();
			  c.add(Calendar.DAY_OF_MONTH, 1);
		}
		return null;
	
	
	}
	
	
	public static String getDateString(Date date,String pattern)
	{
		DateFormat format=  new SimpleDateFormat(pattern);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return format.format(c.getTime());
		
	}
	
	public static Date getStringDate(String str,String pattern)
	{
		DateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	


	/**
	 * 得到当月第一天
	 * @param date
	 * @return
	 */
	public static String getFirstdayOfMonth(Date date)
	{
		return getDateString(date,"yyyy-MM")+"-01";
	}
	/**
	 * 得到某月第一天
	 * @param date
	 * @return
	 */
	public static String getFirstdayOfMonth(String month, String pattern)
	{
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String firstday = "";
		try {
			Date date = format.parse(month);
			firstday = getDateString(date,"yyyy-MM")+"-01";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return firstday;
	}
	/**
	 * 得到某月最后一天
	 * @param date
	 * @return
	 */
	public static String getLastdayOfMonth(String month, String pattern)
	{
		SimpleDateFormat format = new SimpleDateFormat(pattern);  
		String lastday = "";
		try {
			Date date = format.parse(month);  
			Calendar aCalendar = Calendar.getInstance(); 
			aCalendar.setTime(date); 
			aCalendar.add(Calendar.MONTH, 1);
			aCalendar.add(Calendar.DAY_OF_MONTH, -1); 
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			lastday = format1.format(aCalendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lastday;
	}
	
	public int getWeekIndexOfYear(String day)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int index = 0;
		try
		{
			Date startDate = format.parse(day);
			Calendar nowCal = Calendar.getInstance();
			nowCal.setTime(startDate);
			index =  nowCal.get(Calendar.WEEK_OF_YEAR);
		}
		catch (Exception e) {
		}
		return index;
	}
	
	/**
	 * 获得一年的某周的第一天
	 * @param year
	 * @param week_index
	 * @return
	 */
	public static String getFirstDayOfWeek(String year,String week_index,String patten)
	{
		SimpleDateFormat formatter = new  SimpleDateFormat("yyyy w");
		Date date = new Date();
		try {
			date = formatter.parse(year+" "+week_index);
		} catch (ParseException e) {
			return "";
		}
		SimpleDateFormat formatter2 = new  SimpleDateFormat(patten);
		return formatter2.format(date);
		
	}
	
	public static String getLastDayOfWeek(String year,String week_index,String patten)
	{
		String first_day = getFirstDayOfWeek(year,String.valueOf(Integer.valueOf(week_index)+1),patten);
		return getOffsetDay(first_day,-1,patten);
		
	}
	
	public List getWeekList(String year,String week_index,int offset,String patten)
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int current_index = Integer.valueOf(week_index);
		String begin_date = "";
		String end_date = "";
		if(offset>=0)
		{
			for(int i=current_index;i<current_index+offset;i++)
			{
				begin_date = getFirstDayOfWeek(year, String.valueOf(i), patten);
				end_date = getLastDayOfWeek(year, String.valueOf(i), patten);
				Hashtable<String, String> hash = new Hashtable<String, String>();
				hash.put("BEGIN_DATE", begin_date);
				hash.put("END_DATE", end_date);
				list.add(hash);
			}
		}
		else
		{
			for(int i=current_index+offset+1;i<=current_index;i++)
			{
				
				begin_date = getFirstDayOfWeek(year, String.valueOf(i), patten);
				end_date = getLastDayOfWeek(year, String.valueOf(i), patten);
				Hashtable<String, String> hash = new Hashtable<String, String>();
				hash.put("BEGIN_DATE", begin_date);
				hash.put("END_DATE", end_date);
				list.add(hash);
			}
		}
		return list;
	}
	
	public static String getOffsetDay(String currentDay,int offset,String pattern)
	{

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String day = "";
		try
		{
			Date startDate = format.parse(currentDay);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			startCal.add(Calendar.DAY_OF_MONTH, offset-1);
			day = format.format(startCal.getTime());
		
		}catch (Exception e) {
		}
		
		return 	day;
	}
	
	public static void main(String[] args) throws Exception
	{  
		System.out.println(getMonthDay(-1,"yyyyMMdd","20100228")); 
		System.out.println(getMonthDay(-1,"yyyyMMdd","20100131"));
		System.out.println(getMonthDay(-1,"yyyyMMdd","20100331"));
		System.out.println(getMonthDay(-1,"yyyyMMdd","20100430"));
		System.out.println(getMonthDay(-1,"yyyyMMdd","20100402"));
		System.out.println(getMonthDay(-1,"yyyyMMdd","20100217"));
	}

	/**
	 *  eg: getHourList("8:00","10:00","HH:mm",30) 
	 * @return
	 */
	public static List<Map<String, String>> getHourList(String startHour,
	        String endHour, String pattern, int minute)
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		try
		{
			Date startDate = format.parse(startHour);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endHour);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			while (startCal.compareTo(endCal) <= 0)
			{
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String hour = format.format(startCal.getTime());
				hash.put("HOUR", hour);
				list.add(hash);
				startCal.add(Calendar.MINUTE, minute);
			} 
		}
		catch (ParseException e)
		{
		}
		return list;
	}
	
	/**
	 * 得到24小时列表
	 * @return
	 */
	public static List<Map<String, String>> get24HourList(String startHour,
	        String endHour, String pattern, int minute)
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat("HH");
		try
		{
			Date startDate = format.parse(startHour);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endHour);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			while (startCal.compareTo(endCal) <= 0)
			{
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String hour = format.format(startCal.getTime());
				hash.put("HOUR", hour);
				list.add(hash);
				startCal.add(Calendar.MINUTE, minute);
			} 
		}
		catch (ParseException e)
		{
		}
		return list;
	}
	
	/**
	 *  60分钟列表
	 * @return
	 */
	public static List<Map<String, String>> get60MinList(String startMin,
	        String endMin, String pattern, int minute)
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat("mm");
		try
		{
			Date startDate = format.parse(startMin);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endMin);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			while (startCal.compareTo(endCal) <= 0)
			{
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String min = format.format(startCal.getTime());
				hash.put("minute", min);
				list.add(hash);
				startCal.add(Calendar.MINUTE, minute);
			} 
		}
		catch (ParseException e)
		{
		}
		return list;
	}

	/**
	 * 得到星期列表 eg: getWeekList("2008-01-01","2008-01-20","yyyy-MM-dd")
	 * 
	 * @param startday
	 *            开始日期
	 * @param endday
	 *            结束日期
	 * @param pattern
	 *            格式：如yyyy-MM-dd
	 * @return
	 */
	public static List<Map<String, String>> getWeekList(String startday,
			String endday, String pattern)
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try
		{
			Date startDate = format.parse(startday);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endday);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			while (startCal.compareTo(endCal) <= 0)
			{
				Hashtable<String, String> hash = new Hashtable<String, String>();
				SimpleDateFormat fmt = new SimpleDateFormat("E");  
				DateFormatSymbols symbols = fmt.getDateFormatSymbols();
				symbols.setShortWeekdays(new String[]{"","日","一", "二", "三", "四", "五", "六"});
				fmt.setDateFormatSymbols(symbols); 
				hash.put("WEEK", fmt.format(startCal.getTime()));
				list.add(hash);
				startCal.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		catch (ParseException e)
		{  
		}
		return list;
	}
	 
	 
}
