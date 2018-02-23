package secfox.soc.es.search.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.request.ESQueryRequest;
import secfox.soc.es.search.request.ESQueryRequestWrapper;
import secfox.soc.es.search.response.ESQueryHitCountResponse;
import secfox.soc.es.search.response.ESQueryResponse;
import secfox.soc.es.search.service.ESQueryService;
import secfox.soc.netty.HttpSearchMethod;
import secfox.soc.netty.IBaseResponse;
import secfox.soc.util.ApacheBeanUtils;

/**
 * web层查询控制器
 * @author wangzhijie
 *
 */
public class ESQueryController extends BaseController{

	private final static Logger logger = LoggerFactory
			.getLogger(ESQueryController.class);

	private static volatile ESQueryController instance = new ESQueryController();
	
	private ESQueryController() {
	}
	
	public static ESQueryController getInstance(){
		return instance;
	}
	
	private ESQueryFilter filter = new ESQueryFilter();

	@Override
	public ESQueryResponse doFilter(ESQueryRequest request) {
		if(request.getRequestUrl().equals(HttpSearchMethod.SIMQUERY))
			return filter.check(request);
		else if(request.getRequestUrl().equals(HttpSearchMethod.ADVQUERY))
			return filter.checkFuzzy(request);
		else
			return filter.check(request);
	}
	
	public IBaseResponse queryHitCount(Map<String,Object> params) throws Exception{
		ESQueryRequest request = (ESQueryRequest)ApacheBeanUtils.mapToObject(params, ESQueryRequest.class);
		
		ESQueryResponse response = doFilter(request);
		
		if(response.getCode()!=ESQueryResponse.Type.OK.code) return response;

		ESQueryService commQueryService = ESQueryService.getInstance();
		
		ESQueryRequestWrapper esReqWrapper = new ESQueryRequestWrapper(request,params);
		
		return commQueryService.queryHitCount(esReqWrapper);
	}
	
}
