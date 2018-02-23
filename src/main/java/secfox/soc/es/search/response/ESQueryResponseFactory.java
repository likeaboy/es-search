package secfox.soc.es.search.response;

public class ESQueryResponseFactory {
	
	public static ESQueryResponse createResponse(ESQueryResponse.Type type){
		ESQueryResponse resp = new ESQueryResponse();
		resp.setCode(type.code);
		resp.setMsg(type.msg);
		
		return resp;
	}
	
	public static ESQueryResponse createUnkonwErrorResponse(){
		ESQueryResponse resp = new ESQueryResponse();
		resp.setCode(ESQueryResponse.Type.UNKONWERR.code);
		resp.setMsg(ESQueryResponse.Type.UNKONWERR.msg);
		
		return resp;
	}
	
	public static ESQueryResponse createParamsErrorResponse(){
		ESQueryResponse resp = new ESQueryResponse();
		resp.setCode(ESQueryResponse.Type.PARAMERR.code);
		resp.setMsg(ESQueryResponse.Type.PARAMERR.msg);
		
		return resp;
	}
	
	public static ESQueryResponse createSuccRespnse(){
		ESQueryResponse resp = new ESQueryResponse();
		resp.setCode(ESQueryResponse.Type.OK.code);
		resp.setMsg(ESQueryResponse.Type.OK.msg);
		
		return resp;
	}
	
	public static ESQueryResponse dropCurrentReq(){
		ESQueryResponse resp = new ESQueryResponse();
		resp.setCode(ESQueryResponse.Type.DROPREQ.code);
		resp.setMsg(ESQueryResponse.Type.DROPREQ.msg);
		
		return resp;
	}
	
}
