package top.niandui.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 日期处理工具类
 * 
 * @author zhaolei
 * 
 */
public class UtilDate {

	/** 年月日时分秒(无下划线) yyyyMMddHHmmss */
	public static final String dtLong = "yyyyMMddHHmmss";

	/** 完整时间 yyyy-MM-dd HH:mm:ss */
	public static final String simple = "yyyy-MM-dd HH:mm:ss";

	/** 年月日(无下划线) yyyyMMdd */
	public static final String dtShort = "yyyyMMdd";

	/** 完整时间 yyyy-MM-dd */
	public static final String simpleShort = "yyyy-MM-dd";
	/** 年月日时分秒毫秒(无下划线) yyyyMMddHHmmssSSS */
	public static final String dtLongS = "yyyyMMddHHmmssSSS";

	/**
	 * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
	 *
	 * @return 以yyyyMMddHHmmss为格式的当前系统时间
	 */
	public static String DateToStryyyyMMddHHmmssSSS() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(dtLongS);
		return df.format(date);
	}

	/**
	 * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
	 * 
	 * @return 以yyyyMMddHHmmss为格式的当前系统时间
	 */
	public static String DateToStryyyyMMddHHmmss() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(dtLong);
		return df.format(date);
	}

	public static String DateToStr(Date date) {
		DateFormat df = new SimpleDateFormat(simpleShort);
		return df.format(date);
	}

	public static String DateToStrLong(Date date) {
		DateFormat df = new SimpleDateFormat(simple);
		return df.format(date);
	}
	/**
	 * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getDateFormatter() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(simple);
		return df.format(date);
	}
	
	public static long gettimeStampbyDateStr(String dateStr) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = df.parse(dateStr);
         return date.getTime();
	}

	/**
	 * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
	 * 
	 * @return
	 */
	public static String getDate() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(dtShort);
		return df.format(date);
	}

	/**
	 * 产生随机的三位数
	 * 
	 * @return
	 */
	public static String getThree() {
		Random rad = new Random();
		return rad.nextInt(1000) + "";
	}

	public static Date strToDate1(String str) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(str);
	}

	public static Date strToDate2(String str) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(str);
	}
	
	
	static int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };  
	  
	/** 
	 * @param date yyyy-MM-dd HH:mm:ss 
	 * @return 
	 */  
	public static boolean isValidDate(String date) {  
	    try {  
	        int year = Integer.parseInt(date.substring(0, 4));  
	        if (year <= 0)  
	            return false;  
	        int month = Integer.parseInt(date.substring(5, 7));  
	        if (month <= 0 || month > 12)  
	            return false;  
	        int day = Integer.parseInt(date.substring(8, 10));  
	        if (day <= 0 || day > DAYS[month])  
	            return false;  
	        if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {  
	            return false;  
	        }  
	        int hour = Integer.parseInt(date.substring(11, 13));  
	        if (hour < 0 || hour > 23)  
	            return false;  
	        int minute = Integer.parseInt(date.substring(14, 16));  
	        if (minute < 0 || minute > 59)  
	            return false;  
	        int second = Integer.parseInt(date.substring(17, 19));  
	        if (second < 0 || second > 59)  
	            return false;  
	  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	        return false;  
	    }  
	    return true;  
	}  
	public static final boolean isGregorianLeapYear(int year) {  
	    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);  
		}  

	/**
	* @Title 根据给定时间给出该时间所在周的周日时间
	* @time 2018-3-12 下午7:57:23
	 */
	public static Date convertWeekByDate(Date time) {  
		

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式  

        Calendar cal = Calendar.getInstance();  

        cal.setTime(time);  

       //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  

       int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  

       if(1 == dayWeek) {  

           cal.add(Calendar.DAY_OF_MONTH, -1);  

       }  

       cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  

       int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  

       cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   
       
       String imptimeBegin = sdf.format(cal.getTime());  

       System.out.println("所在周星期一的日期："+imptimeBegin);  

       cal.add(Calendar.DATE, 6);  

       String imptimeEnd = sdf.format(cal.getTime());  
       
       return cal.getTime();

	}  
	
	/**获取某月最后一天的日期
	* @Title UtilDate.java 
	* @description TODO 
	* @return Date 
	* @time 2018-3-13 上午11:44:50
	 */
	public static Date getLastDayOfMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDayOfMonth = calendar.getTime();  
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDayOfMonth = calendar.getTime();
		//System.out.println(format1.format(firstDayOfMonth));
		return lastDayOfMonth;
	}
}
