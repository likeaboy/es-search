package secfox.soc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import secfox.soc.es.search.service.bean.Slot;

public class CalendarUtils {
	
	//默认5分钟时间间隔
	public static final long TIME_RANGE = 5 * 60 * 1000L;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String logRangeTime(Slot currentSlot){
		if(currentSlot == null)
			return "current slot is null";
		return logRangeTime(currentSlot.getStartTime(),currentSlot.getEndTime());
	}
	
	public static String logRangeTime(long startTime,long endTime){
		StringBuilder record = new StringBuilder();
		record.append(sdf.format(new Date(startTime)));
		record.append("~");
		record.append(sdf.format(new Date(endTime)));
		return record.toString();
	}

	/**
	 * 获取当天最小时间 00:00:00
	 * @param c
	 */
	public static void toTodayEarlyTime(Calendar c){
		c.set(Calendar.HOUR_OF_DAY,  
                c.getActualMinimum(Calendar.HOUR_OF_DAY));  
        c.set(Calendar.MINUTE,  
                c.getActualMinimum(Calendar.MINUTE));  
        c.set(Calendar.SECOND,  
                c.getActualMinimum(Calendar.SECOND));
	}
	/**
	 * 获取当天最大时间，即23:59:59
	 * @param c
	 */
	public static void toTodayLastTime(Calendar c){
		c.set(Calendar.HOUR_OF_DAY,  
				c.getActualMaximum(Calendar.HOUR_OF_DAY));  
		c.set(Calendar.MINUTE,  
				c.getActualMaximum(Calendar.MINUTE));  
		c.set(Calendar.SECOND,  
				c.getActualMaximum(Calendar.SECOND)); 
	}
	
	public static void toHourLastTime(Calendar c){
		c.set(Calendar.MINUTE,  
				c.getActualMaximum(Calendar.MINUTE));  
		c.set(Calendar.SECOND,  
				c.getActualMaximum(Calendar.SECOND)); 
	}
	
	public static void toHourEarlyTime(Calendar c){
        c.set(Calendar.MINUTE,  
                c.getActualMinimum(Calendar.MINUTE));  
        c.set(Calendar.SECOND,  
                c.getActualMinimum(Calendar.SECOND));
	}
}
