package secfox.soc.alarm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class AlarmRequest {

	private String phone;
	private String port = "/dev/ttyUSB0";
	private String bps = "115200";
	private String content = "360告警处置中心测试短信!";
	private List<String> phoneList;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getBps() {
		return bps;
	}
	public void setBps(String bps) {
		this.bps = bps;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public List<String> convert(){
		if(phoneList != null)
			return phoneList;
		List<String> list = new ArrayList<String>();
		if(StringUtils.isEmpty(phone))
			return list;
		String[] element = phone.split(",");
		for(String e : element){
			list.add(e);
		}
		phoneList = list;
		return list;
	}
	
	public List<String> getPhoneList(){
		return phoneList;
	}
}
