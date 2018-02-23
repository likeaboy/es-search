package secfox.soc.es.search.request;

import java.util.Map;

public class ESQueryRequestWrapper {

	private ESQueryRequest request;
	private Map<String,Object> params;
	
	public ESQueryRequestWrapper(ESQueryRequest request,Map<String,Object> params){
		this.request = request;
		this.params = params;
	}

	public ESQueryRequest getRequest() {
		return request;
	}

	public void setRequest(ESQueryRequest request) {
		this.request = request;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
}
