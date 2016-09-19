package cn.smile.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * @author  smile
 */
public final class TimeUtil {

	public final static String FORMAT_YEAR = "yyyy";
	public final static String FORMAT_MONTH_DAY = "MM-dd";
	public final static String FORMAT_DATE = "yyyy-MM-dd";
	public final static String FORMAT_TIME = "HH:mm";
	public final static String FORMAT_MONTH_DAY_TIME = "MM-dd HH:mm";
	public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
	public final static String FORMAT_DATE_TIME_SECOND = "yyyy-MM-dd HH:mm:ss";

	private static final int YEAR = 365 * 24 * 60 * 60;// 1年
	private static final int MONTH = 30 * 24 * 60 * 60;// 1月
	public static final int DAY = 24 * 60 * 60;// 1天
	private static final int HOUR = 60 * 60;// 1小时
	private static final int MINUTE = 60;// 1分钟

	/**
	 * 根据时间戳获取描述性时间，如3分钟前，1天前
	 * @param timestamp 时间戳 单位为毫秒
	 * @return 时间字符串
	 */
	public static String getFormatTime(long timestamp) {
		long currentTime = System.currentTimeMillis();
		long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
		String timeStr;
		if (timeGap > YEAR) {
			timeStr = timeGap / YEAR + "年前";
		} else if (timeGap > MONTH) {
			timeStr = timeGap / MONTH + "个月前";
		} else if (timeGap > DAY) {// 1天以上
			timeStr = timeGap / DAY + "天前";
		} else if (timeGap > HOUR) {// 1小时-24小时
			timeStr = timeGap / HOUR + "小时前";
		} else if (timeGap > MINUTE) {// 1分钟-59分钟
			timeStr = timeGap / MINUTE + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}

	/** 获取聊天时间：因为sdk的时间默认到秒故应该乘1000
	 * @param hasYear :是否包含年份
	 * @param timesamp
	 */
	public static String getChatTime(boolean hasYear,long timesamp) {
		long clearTime = timesamp*1000;
		String result;
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(clearTime);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));
		switch (temp) {
			case 0:
				result = "今天 " + getHourAndMin(clearTime);
				break;
			case 1:
				result = "昨天 " + getHourAndMin(clearTime);
				break;
			case 2:
				result = "前天 " + getHourAndMin(clearTime);
				break;
			default:
				result = getTime(hasYear,clearTime);
				break;
		}
		return result;
	}

	/**
	 * @param hasYear
	 * @param time
	 * @return
	 */
	private static String getTime(boolean hasYear,long time) {
		String pattern=FORMAT_DATE_TIME;
		if(!hasYear){
			pattern = FORMAT_MONTH_DAY_TIME;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date(time));
	}

	/**
	 * 当前时间与目标时间对比，是否过了gapTime
	 * @param targetTime 目标时间
	 * @param gapTime 间隔时间 单位：秒，比如是否过了10分钟，则gapTime为60*10
     * @return true-超过，false-没超过
     */
	public static boolean isGap(long targetTime,int gapTime){
		long currentTime=System.currentTimeMillis();
		long timeGap = (currentTime - targetTime) / 1000;//单位：秒
		if(timeGap > gapTime){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 获取当前日期的指定格式的字符串
	 * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
	 * @return
	 */
	public static String getCurrentTime(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		if (format == null || format.trim().equals("")) {
			sdf.applyPattern(FORMAT_DATE_TIME);
		} else {
			sdf.applyPattern(format);
		}
		return sdf.format(new Date());
	}

	/**
	 * date类型转换为String类型
	 * @param data
	 * @param formatType
     * @return
     */
	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}

	/**
	 * long类型转换为String类型
	 * @param currentTime 要转换的long类型的时间
	 * @param formatType 要转换的string类型的时间格式
     * @return
     */
	public static String longToString(long currentTime, String formatType){
		Date date = longToDate(currentTime, formatType);// long类型转成Date类型
		return dateToString(date, formatType); // date类型转成String
	}

	/**
	 * string类型转换为date类型
	 * @param strTime 时间格式必须要与formatType相同
	 * @param formatType 要转换的string类型的时间
     * @return
     */
	public static Date stringToDate(String strTime, String formatType){
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * long转换为Date类型
	 * @param currentTime 要转换的long类型的时间
	 * @param formatType
     * @return
     */
	public static Date longToDate(long currentTime, String formatType){
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		return stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
	}

	/**
	 * string类型转换为long类型
	 * @param strTime 要转换的时间
	 * @param formatType
     * @return
     */
	public static long stringToLong(String strTime, String formatType){
		long time=0;
		Date date = stringToDate(strTime, formatType);
		if (date != null) {
			time = dateToLong(date);
		}
		return time;
	}

	/**
	 * @param date
	 * @return
     */
	public static long dateToLong(Date date) {
		if(date!=null){
			return date.getTime();
		}else{
			return 0;
		}
	}

	/**
	 * @param time
	 * @return
     */
	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_TIME);
		return format.format(new Date(time));
	}

	/**比较时间
	 * @param startYear 开始年
	 * @param startMonth 开始月
	 * @param endYear 结束年
	 * @param endMonth 结束月
	 */
	public static int compare(int startYear,int startMonth,int endYear,int endMonth){
		if(startYear!=0 && endYear!=0){
			if(startYear>endYear){//比较年份
				return 1;//开始时间大于结束时间
			}else if(startYear==endYear){//相等
				if(startMonth>=endMonth){//比较月份
					return 1;
				}else if(startMonth==endMonth){
					return 0;
				}else{//开始时间小于结束时间
					return -1;
				}
			}else{//
				return -1;//开始时间小于结束时间
			}
		}else{
			return -1;
		}
	}

	/**将BmobDate转换为指定格式的时间
	 * @param bmobDate string格式的日期字符串
	 * @param formatType 转换格式
	 * @return
	 */
	public static String getBmobDate(String bmobDate,String formatType){
		SimpleDateFormat formatter = new SimpleDateFormat(TimeUtil.FORMAT_DATE_TIME_SECOND);
		Date date = null;
		try {
			date = formatter.parse(bmobDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(date!=null) {
			return new SimpleDateFormat(formatType).format(date);
		}else {
			return null;
		}
	}

}