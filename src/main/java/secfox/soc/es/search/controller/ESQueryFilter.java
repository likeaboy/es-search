package secfox.soc.es.search.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.request.ESQueryRequest;
import secfox.soc.es.search.response.ESQueryResponse;
import secfox.soc.es.search.response.ESQueryResponseFactory;
import secfox.soc.netty.HttpSearchMethod;

/**
 * 过滤器
 * @author wangzhijie
 *
 */
public class ESQueryFilter {
	
	private final static Logger logger = LoggerFactory
			.getLogger(ESQueryFilter.class);

	public ESQueryFilter() {
	}

	/**
	 * 
	 * @return
	 */
	public ESQueryResponse check(ESQueryRequest request){
		
		if(request.getRequestUrl().equals(HttpSearchMethod.FETCH))
			request.setFetchOnlyOnce(true);
		//check
		/*if(StringUtils.isEmpty(request.getdAddr())){
			return ESQueryResponseFactory.createParamsErrorResponse();
		}*/
		
		return ESQueryResponseFactory.createSuccRespnse();
	}
	
	public ESQueryResponse checkFuzzy(ESQueryRequest request){
		
		//check
		if(StringUtils.isEmpty(request.getKeyWords())){
			return ESQueryResponseFactory.createParamsErrorResponse();
		}
		
		return ESQueryResponseFactory.createSuccRespnse();
	}
	
}
