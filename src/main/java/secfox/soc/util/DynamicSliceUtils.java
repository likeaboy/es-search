package secfox.soc.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.ESClient;
import secfox.soc.es.search.service.bean.Idx4SlotWrapper;
import secfox.soc.es.search.service.bean.Slot;
import secfox.soc.es.search.service.bean.TimePair;
import secfox.soc.es.search.service.sorter.AscSorter;
import secfox.soc.es.search.service.sorter.DescSorter;
/**
 * 动态切分时间工具类
 * @author wangzhijie
 *
 */
public class DynamicSliceUtils {
	
	private final static Logger logger = LoggerFactory.getLogger(DynamicSliceUtils.class);
	
	public static List<TimePair> sliceTime(long startTime,long endTime){
		TimePair t = getTime(startTime,endTime);
		List<TimePair> dayRanges = new ArrayList<TimePair>();
		
		int range = getAcrossRange(t,Calendar.DAY_OF_YEAR);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(new Date(t.getStartTime()));
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(new Date(t.getEndTime()));
		
		switch(range){
		//same day
		case 0:
			dayRanges.add(t);
			return dayRanges;
		case 1:
			TimePair startDay = new TimePair();
			startDay.setStartTime(startCalendar.getTime().getTime());
			CalendarUtils.toTodayLastTime(startCalendar);
			startDay.setEndTime(startCalendar.getTime().getTime());
			dayRanges.add(startDay);
			
			TimePair endDay = new TimePair();
			endDay.setEndTime(endCalendar.getTime().getTime());
			CalendarUtils.toTodayEarlyTime(endCalendar);
			endDay.setStartTime(endCalendar.getTime().getTime());
			dayRanges.add(endDay);
			return dayRanges;
		default :
			//across day,切分index
			//range+1 相差1天但是要算两个timePair
			for(int i=0;i<range+1;i++){
				TimePair day = new TimePair();
				
				if(i == 0){
					day.setStartTime(startCalendar.getTime().getTime());
					CalendarUtils.toTodayLastTime(startCalendar);
					day.setEndTime(startCalendar.getTime().getTime());
					dayRanges.add(day);
					continue;
				}
				
				if(i == range){
					day.setEndTime(endCalendar.getTime().getTime());
					CalendarUtils.toTodayEarlyTime(endCalendar);
					day.setStartTime(endCalendar.getTime().getTime());
					dayRanges.add(day);
					continue;
				}
				startCalendar.add(Calendar.DAY_OF_MONTH, 1);
				CalendarUtils.toTodayEarlyTime(startCalendar);
				day.setStartTime(startCalendar.getTime().getTime());
				CalendarUtils.toTodayLastTime(startCalendar);
				day.setEndTime(startCalendar.getTime().getTime());
				dayRanges.add(day);
			}
			return dayRanges; 
		}
	}
	
	public static List<Slot> sliceDay(TimePair day){
		List<Slot> slots = new ArrayList<Slot>();
		int margin = getAcrossRange(day,Calendar.HOUR_OF_DAY);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(new Date(day.getStartTime()));
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(new Date(day.getEndTime()));
		
		switch(margin){
		case 0:
			slots.add(new Slot(startCalendar.getTime().getTime(),
					endCalendar.getTime().getTime()));
			return slots;
		case 1:
			Slot startSlot = new Slot();
			startSlot.setStartTime(startCalendar.getTime().getTime());
			CalendarUtils.toHourLastTime(startCalendar);
			startSlot.setEndTime(startCalendar.getTime().getTime());
			slots.add(startSlot);
			
			Slot endDay = new Slot();
			endDay.setEndTime(endCalendar.getTime().getTime());
			CalendarUtils.toHourEarlyTime(endCalendar);
			endDay.setStartTime(endCalendar.getTime().getTime());
			slots.add(endDay);
			return slots;
		default :
			for(int i=0;i<margin+1;i++){
				Slot hour = new Slot();
				if(i == 0){
					hour.setStartTime(startCalendar.getTime().getTime());
					CalendarUtils.toHourLastTime(startCalendar);
					hour.setEndTime(startCalendar.getTime().getTime());
					slots.add(hour);
					continue;
				}
				
				if(i == margin){
					hour.setEndTime(endCalendar.getTime().getTime());
					CalendarUtils.toHourEarlyTime(endCalendar);
					hour.setStartTime(endCalendar.getTime().getTime());
					slots.add(hour);
					continue;
				}
				startCalendar.add(Calendar.HOUR_OF_DAY, 1);
				CalendarUtils.toHourEarlyTime(startCalendar);
				hour.setStartTime(startCalendar.getTime().getTime());
				CalendarUtils.toHourLastTime(startCalendar);
				hour.setEndTime(startCalendar.getTime().getTime());
				slots.add(hour);
				
			}
			return slots;
		}
	}
	
	public static Comparator<String> getComparator(boolean isAsc){
		return isAsc ? new AscSorter():new DescSorter();
	} 
	
	public static Map<String,TimePair> slice(long startTime,long endTime){
		Map<String,TimePair> idx4Time = new TreeMap<String,TimePair>();
		
		//根据时间切分，返回按天切分集合
		List<TimePair> dayRanges = sliceTime(startTime,endTime);
		
		//skip所有不存在的index
		dayRanges = skipIndexes(dayRanges);
		
		for(TimePair day : dayRanges){
			String index = IndexGenerater.generate(day);
			idx4Time.put(index, day);
		}
		
		return idx4Time;
	}
	
	
	public static Idx4SlotWrapper slice(long startTime,long endTime,boolean isAsc,String hitIndex,String hitSlotKey,int slotFrom){
		Map<String,List<Slot>> idx4slots = new TreeMap<String,List<Slot>>(getComparator(isAsc));
		//根据时间切分，返回按天切分集合
		List<TimePair> dayRanges = sliceTime(startTime,endTime);
		
		//skip所有不存在的index
		dayRanges = skipIndexes(dayRanges);
		
		for(TimePair day : dayRanges){
			//按小时分，一个小时为一个slot
			List<Slot> slots = sliceDay(day);
			//desc
			if(!isAsc) Collections.reverse(slots);
			String index = IndexGenerater.generate(day);
			idx4slots.put(index, slots);
		}
		
		return new Idx4SlotWrapper(idx4slots,isAsc,hitIndex,hitSlotKey,slotFrom);
	}
	
	private static List<TimePair> skipIndexes(List<TimePair> dayRanges){
		List<TimePair> newDayRanges = new ArrayList<TimePair>();
		Set<String> existIndexes = ESClient.getInstance().getSkyeyeLasIndexes();
		
		for(TimePair t : dayRanges){
			StringBuilder index = new StringBuilder(IndexGenerater.getIndexPrefix());
			String indexSuffix = IndexGenerater.getIndexSuffixDateFormat().format(new Date(t.getStartTime()));
			index.append(indexSuffix);
			if(existIndexes.contains(index.toString())) newDayRanges.add(t);
		}
		
		return newDayRanges;
	}
	
	public static int getAcrossRange(TimePair t,int calendarField){
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(new Date(t.getStartTime()));
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(new Date(t.getEndTime()));
		return endCalendar.get(calendarField) - startCalendar.get(calendarField);
	}
	
	public static boolean isAcross(TimePair t,int calendarField){
		return getAcrossRange(t,calendarField) == 0 ? false : true;
	}
	
	/**
	 * 根据开始，结束时间获取timepair对象
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private static TimePair getTime(long startTime,long endTime){
		if(startTime == 0 && endTime == 0){
			endTime = System.currentTimeMillis();
			startTime = endTime - CalendarUtils.TIME_RANGE;
		}else if (startTime == 0 && endTime != 0){
			startTime = endTime - CalendarUtils.TIME_RANGE;
		}else if(startTime != 0 && endTime == 0){
			endTime = startTime + CalendarUtils.TIME_RANGE;
		}
		return new TimePair(startTime,endTime);
	}
}
