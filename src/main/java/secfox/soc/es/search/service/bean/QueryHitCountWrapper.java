package secfox.soc.es.search.service.bean;

import java.util.Map;

public class QueryHitCountWrapper {

	private TimePair queryTimePair;
	private String hitIndex;
	private Map<String,TimePair> idx4Time;
	
	public QueryHitCountWrapper(TimePair queryTimePair,String hitIndex,Map<String,TimePair> idx4Time){
		this.queryTimePair = queryTimePair;
		this.hitIndex = hitIndex;
		this.idx4Time = idx4Time;
	}
	
	public TimePair getQueryTimePair() {
		return queryTimePair;
	}
	public void setQueryTimePair(TimePair queryTimePair) {
		this.queryTimePair = queryTimePair;
	}
	public String getHitIndex() {
		return hitIndex;
	}
	public void setHitIndex(String hitIndex) {
		this.hitIndex = hitIndex;
	}
	public Map<String, TimePair> getIdx4Time() {
		return idx4Time;
	}
	public void setIdx4Time(Map<String, TimePair> idx4Time) {
		this.idx4Time = idx4Time;
	}
	
	
}
