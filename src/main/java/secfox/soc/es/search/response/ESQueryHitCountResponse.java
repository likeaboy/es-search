package secfox.soc.es.search.response;

import secfox.soc.netty.IBaseResponse;

public class ESQueryHitCountResponse implements IBaseResponse{
	private int code;
	private String msg;
	private long count;
	private String hitIndex;
	private int isMore = 1;
	
	public ESQueryHitCountResponse(int code, String msg, long count,
			String hitIndex, int isMore) {
		super();
		this.code = code;
		this.msg = msg;
		this.count = count;
		this.hitIndex = hitIndex;
		this.isMore = isMore;
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


	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getHitIndex() {
		return hitIndex;
	}

	public void setHitIndex(String hitIndex) {
		this.hitIndex = hitIndex;
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
		return "ESQueryHitCountResponse [code=" + code + ", msg=" + msg + ", count="
				+ count + ", hitIndex=" + hitIndex + ", isMore=" + isMore + "]";
	}
	
}
