package secfox.soc.es.search.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.request.ESQueryRequest;
import secfox.soc.es.search.request.ESQueryRequestWrapper;
import secfox.soc.es.search.response.ESQueryResponse;
import secfox.soc.es.search.service.ESQueryService;
import secfox.soc.util.ApacheBeanUtils;

public abstract class BaseController {
	
	private final static Logger logger = LoggerFactory
			.getLogger(BaseController.class);

	public ESQueryResponse query(Map<String,Object> params) throws Exception{
		ESQueryRequest request = (ESQueryRequest)ApacheBeanUtils.mapToObject(params, ESQueryRequest.class);
		
		ESQueryResponse response = doFilter(request);
		
		if(response.getCode()!=ESQueryResponse.Type.OK.code) return response;

		ESQueryService commQueryService = ESQueryService.getInstance();
		
		ESQueryRequestWrapper esReqWrapper = new ESQueryRequestWrapper(request,params);
		
		return commQueryService.query(esReqWrapper);
	}
	
	public abstract ESQueryResponse doFilter(ESQueryRequest request);
}
