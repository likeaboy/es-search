package secfox.soc.es.search.controller;

import java.util.Map;

import secfox.soc.es.search.request.ESQueryRequestParam;

public class ParmFilter {
	private static String[] KEYS = {
		ESQueryRequestParam.KEY_STARTTIME,
		ESQueryRequestParam.KEY_ENDTIME,
		ESQueryRequestParam.KEY_DEVADDR,
		ESQueryRequestParam.KEY_SADDR,
		ESQueryRequestParam.KEY_SPORT,
		ESQueryRequestParam.KEY_DADDR,
		ESQueryRequestParam.KEY_DADDR,
		ESQueryRequestParam.KEY_DPORT,
		ESQueryRequestParam.KEY_ID,
		ESQueryRequestParam.KEY_SUSERNAME,
		ESQueryRequestParam.KEY_STADDR,
		ESQueryRequestParam.KEY_STPORT,
		ESQueryRequestParam.KEY_DTADDR,
		ESQueryRequestParam.KEY_DTPORT,
		ESQueryRequestParam.KEY_SYSTYPE,
		ESQueryRequestParam.KEY_PRIORITY,
		ESQueryRequestParam.KEY_DEVTYPE,
		ESQueryRequestParam.KEY_DEVCATEGORY,
		ESQueryRequestParam.KEY_ACTION,
		ESQueryRequestParam.KEY_RESULT,
		ESQueryRequestParam.KEY_KEYWORDS
	};
	
	public static void fill(Map<String,Object> condMap){
		for(String key : KEYS){
			if(!condMap.containsKey(key)){
				condMap.put(key, "");
			}
		}
	}
}
