package secfox.soc.alarm;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.request.ESQueryRequest;
import secfox.soc.util.ApacheBeanUtils;

public class AlarmFilter {
	
	private final static Logger logger = LoggerFactory
			.getLogger(AlarmFilter.class);
	
	public static final String PHONE = "phone";
	public static final String PORT = "port";
	public static final String BPS = "bps";
	public static final String CONTENT = "content";

	public AlarmFilter() {
	}

	public AlarmResponse checkParams(AlarmRequest alarmRequest){
	     
	     if(StringUtils.isEmpty(alarmRequest.getPhone())){
	    	 AlarmResponse alarmResp = new AlarmResponse();
	    	 alarmResp.setCode(AlarmResponse.Type.PARAMERR.code);
	         alarmResp.setMsg(AlarmResponse.Type.PARAMERR.msg);
	         return alarmResp;
	     }
	     
	     AlarmResponse alarmResp = new AlarmResponse();
    	 alarmResp.setCode(AlarmResponse.Type.OK.code);
         alarmResp.setMsg(AlarmResponse.Type.OK.msg);
	     
	     return alarmResp;
	}
	
	public AlarmRequest convert(Map<String, Object> params) throws Exception{
		return (AlarmRequest)ApacheBeanUtils.mapToObject(params, AlarmRequest.class);
	}
	
}
