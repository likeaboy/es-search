package secfox.soc.es.common;

import secfox.soc.netty.IBaseResponse;

public class ESGetResponse implements IBaseResponse{

	public ESGetResponse(){
		this.msg = "";
	}
	
	public ESGetResponse(int code,String msg){
		this.code = code;
		this.msg = msg;
	}
	
	private int code;
	private String msg;
	//是否与NGSOC兼容	
	private int isCompatibility;
	private String esIp;
	//日志清除阈值
	private String delLogFactor;
	//日志告警阈值
	private String alarmLogFactor;
	
	public enum Type{
		ERROR(500,"错误"),
		PARAMERR(400,"参数不合法"),
		INVAIDQUERY(401,"查询url不合法"),
		DROPREQ(402,"不处理图标资源请求"),
		OK(200,"查询成功");
		
		public final int code;
		public final String msg;
		
		private Type(int code,String msg){
			this.code = code;
			this.msg =msg;
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getIsCompatibility() {
		return isCompatibility;
	}

	public void setIsCompatibility(int isCompatibility) {
		this.isCompatibility = isCompatibility;
	}

	public String getEsIp() {
		return esIp;
	}

	public void setEsIp(String esIp) {
		this.esIp = esIp;
	}

	public String getDelLogFactor() {
		return delLogFactor;
	}

	public void setDelLogFactor(String delLogFactor) {
		this.delLogFactor = delLogFactor;
	}

	public String getAlarmLogFactor() {
		return alarmLogFactor;
	}

	public void setAlarmLogFactor(String alarmLogFactor) {
		this.alarmLogFactor = alarmLogFactor;
	}

	@Override
	public String toString() {
		return "ESGetResponse [code=" + code + ", msg=" + msg
				+ ", isCompatibility=" + isCompatibility + ", esIp=" + esIp
				+ ", delLogFactor=" + delLogFactor + ", alarmLogFactor="
				+ alarmLogFactor + "]";
	}
}
