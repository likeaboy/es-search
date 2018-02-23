package secfox.soc.es.search.service.bean;

public class TimePair {

	private long startTime;
	private long endTime;
	
	public TimePair(){}
	
	public TimePair(long startTime,long endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
}
