package secfox.soc.es.common;

import secfox.soc.netty.IBaseResponse;

public class ESModifyResponse implements IBaseResponse{

	public ESModifyResponse(){
		this.msg = "";
	}
	
	public ESModifyResponse(int code,String msg){
		this.code = code;
		this.msg = msg;
	}
	
	private int code;
	private String msg;
	
	public enum Type{
		ERROR(500,"错误"),
		PARAMERR(400,"参数不合法"),
		INVAIDQUERY(401,"查询url不合法"),
		DROPREQ(402,"不处理图标资源请求"),
		OK(200,"修改成功");
		
		public final int code;
		public final String msg;
		
		private Type(int code,String msg){
			this.code = code;
			this.msg =msg;
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ESCommonResponse [code=" + code + ", msg=" + msg + "]";
	}
}
