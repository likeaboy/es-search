package secfox.soc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import secfox.soc.es.search.service.bean.TimePair;

public class IndexGenerater {
	
	private static final String ES_IDX_PREFIX = "skyeye-las_event";

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	
	public static String generate(TimePair t){
		StringBuilder idx = new StringBuilder(ES_IDX_PREFIX);
		idx.append("-");
		idx.append(sdf.format(new Date(t.getEndTime())));
		return idx.toString();
	}
	
	public static String getCurrentDayIndex(){
		StringBuilder idx = new StringBuilder(ES_IDX_PREFIX);
		idx.append("-");
		idx.append(sdf.format(new Date()));
		return idx.toString();
	}
	
	public static String getIndexFarFromToday(int farFrom){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_YEAR, farFrom);
		StringBuilder idx = new StringBuilder(ES_IDX_PREFIX);
		idx.append("-");
		idx.append(sdf.format(c.getTime()));
		return idx.toString();
	}
	
	public static SimpleDateFormat getIndexSuffixDateFormat(){
		return sdf;
	}
	
	public static String getIndexPrefix(){
		return ES_IDX_PREFIX + "-";
	}
}
