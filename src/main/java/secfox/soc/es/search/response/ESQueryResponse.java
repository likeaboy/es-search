package secfox.soc.es.search.response;

import secfox.soc.netty.IBaseResponse;

public class ESQueryResponse implements IBaseResponse{
	
	public ESQueryResponse(){
		this.msg = "";
		this.data = "";
	}
	
	public ESQueryResponse(int code,String msg,int count,String data){
		this.code = code;
		this.msg = msg;
		this.count = count;
		this.data = data;
	}
	
	private int code;
	private String msg;
	private int count;
	private String data;
	
	private String hitIndex;
	private String hitSlotKey;
	private int slotFrom;
	
	private int isMore = 1;
	
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public String getHitIndex() {
		return hitIndex;
	}

	public void setHitIndex(String hitIndex) {
		this.hitIndex = hitIndex;
	}

	public String getHitSlotKey() {
		return hitSlotKey;
	}

	public void setHitSlotKey(String hitSlotKey) {
		this.hitSlotKey = hitSlotKey;
	}

	public int getSlotFrom() {
		return slotFrom;
	}

	public void setSlotFrom(int slotFrom) {
		this.slotFrom = slotFrom;
	}

	public int getIsMore() {
		return isMore;
	}
	
	public void setIsMore(int isMore) {
		this.isMore = isMore;
	}

	public enum Type{
		QUERYERR(500,"服务端查询错误"),
		UNKONWERR(501,"未知错误"),
		PARAMERR(400,"参数不合法"),
		INVAIDQUERY(401,"查询url不合法"),
		DROPREQ(402,"不处理图标资源请求"),
		OK(200,"查询成功");
		
		public final int code;
		public final String msg;
		
		private Type(int code,String msg){
			this.code = code;
			this.msg =msg;
		}
	}
	
	@Override
	public String toString() {
		return "ESQueryResponse [code=" + code + ", msg=" + msg + ", count="
				+ count + ", hitIndex=" + hitIndex + ", hitSlotKey="
				+ hitSlotKey + ", slotFrom=" + slotFrom + ", isMore=" + isMore
				+ "]";
	}
	
}
