package secfox.soc.es.common;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESModifyConfigFilter {
	private final static Logger logger = LoggerFactory
			.getLogger(ESModifyConfigFilter.class);
	
	/**
	 * 
	 * @return
	 */
	public ESModifyResponse check(Map<String,Object> params){
		
		ESModifyResponse resp = new ESModifyResponse();
		
		if(params.containsKey(ESModifyConfigParams.PARAM_DELLOGFACTOR) && 
				params.containsKey(ESModifyConfigParams.PARAM_ALARMLOGFACTOR)){
			String clearValRatio = String.valueOf(params.get(ESModifyConfigParams.PARAM_DELLOGFACTOR));
			String alarmValRatio = String.valueOf(params.get(ESModifyConfigParams.PARAM_ALARMLOGFACTOR));
			if(StringUtils.isNotEmpty(clearValRatio) && 
					StringUtils.isNotEmpty(alarmValRatio)){
				if(Integer.parseInt(clearValRatio) > 100){
					resp.setCode(ESModifyResponse.Type.PARAMERR.code);
					resp.setMsg(ESModifyResponse.Type.PARAMERR.msg);
					logger.info("[modify config params error]");
					return resp;
				}
				if(Integer.parseInt(alarmValRatio) > 100){
					resp.setCode(ESModifyResponse.Type.PARAMERR.code);
					resp.setMsg(ESModifyResponse.Type.PARAMERR.msg);
					logger.info("[modify config params error]");
					return resp;
				}
			}
		}
		
		resp.setCode(ESModifyResponse.Type.OK.code);
		resp.setMsg(ESModifyResponse.Type.OK.msg);
		
		return resp;
	}
}
