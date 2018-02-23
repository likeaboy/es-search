package secfox.soc.netty.server.handler;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.alarm.AlarmController;
import secfox.soc.es.common.ESCommonController;
import secfox.soc.es.search.controller.ESQueryController;
import secfox.soc.es.search.controller.ParmFilter;
import secfox.soc.es.search.response.ESQueryResponse;
import secfox.soc.es.search.response.ESQueryResponseFactory;
import secfox.soc.es.search.service.exception.UnkownSearchException;
import secfox.soc.netty.HttpSearchMethod;
import secfox.soc.netty.IBaseResponse;
import secfox.soc.netty.server.NettyFullRequestParser;

/**
 * 分发http请求到controller
 * @author wangzhijie
 *
 */
public class HttpDispatcherController {
	private final static Logger logger = LoggerFactory
			.getLogger(HttpDispatcherController.class);

	private static volatile HttpDispatcherController instance = new HttpDispatcherController();
	
	private HttpDispatcherController() {
	}
	
	public static HttpDispatcherController getInstance(){
		return instance;
	}
	
	public IBaseResponse route(FullHttpRequest fullReq){
		// 将GET, POST所有请求参数转换成Map对象
		Map<String, Object> params;
		ESQueryResponse esqResp = new ESQueryResponse();
		try {
			params = new NettyFullRequestParser(fullReq).parse();
		
			logger.info("[query params : " + params + "]");
			// check
			ParmFilter.fill(params);
			String url = fullReq.getUri();
			int separate = url.indexOf('?');
			if(separate > 0){
				url = url.substring(0, separate);
			}
			
			// common query
			switch(url){
				case HttpSearchMethod.HITCOUNT :
					params.put(HttpSearchMethod.REQUEST_URL, url);
					return ESQueryController.getInstance().queryHitCount(params);
				case HttpSearchMethod.SIMQUERY : 
				case HttpSearchMethod.ADVQUERY : 
				case HttpSearchMethod.FETCH : 
				case HttpSearchMethod.TEST : 
					params.put(HttpSearchMethod.REQUEST_URL, url);
					return ESQueryController.getInstance().query(params);
				case HttpSearchMethod.FAV : 
					//do noting
					return ESQueryResponseFactory.dropCurrentReq();
				case HttpSearchMethod.ALARM : 
					params.put(HttpSearchMethod.REQUEST_URL, url);
					return AlarmController.getInstance().directCallPython(params);
				case HttpSearchMethod.ES_MODIFY : 
					params.put(HttpSearchMethod.REQUEST_URL, url);
					return ESCommonController.getInstance().modify(params);
				case HttpSearchMethod.ES_GET : 
					params.put(HttpSearchMethod.REQUEST_URL, url);
					return ESCommonController.getInstance().get();
				default:
					//invalid query
					esqResp.setCode(ESQueryResponse.Type.INVAIDQUERY.code);
					esqResp.setMsg(ESQueryResponse.Type.INVAIDQUERY.msg);
					esqResp.setCount(0);
					esqResp.setData("");
					return esqResp;
			}
		} catch (UnkownSearchException ue) {
			esqResp.setCode(ESQueryResponse.Type.UNKONWERR.code);
			esqResp.setMsg(ESQueryResponse.Type.UNKONWERR.msg);
			esqResp.setCount(0);
			esqResp.setData("");
			logger.error("["+esqResp.toString()+"]",ue);
		} catch(Exception e){
			esqResp.setCode(ESQueryResponse.Type.QUERYERR.code);
			esqResp.setMsg(ESQueryResponse.Type.QUERYERR.msg);
			esqResp.setCount(0);
			esqResp.setData("");
			logger.error("["+esqResp.toString()+"]",e);
		}
		return esqResp;
	}
}
