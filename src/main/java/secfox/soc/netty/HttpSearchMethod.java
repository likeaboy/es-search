package secfox.soc.netty;

public class HttpSearchMethod {
	
	public static final String REQUEST_URL = "requestUrl";

	//普通查询
	public static final String SIMQUERY = "/simquery";
	//高级查询
	public static final String ADVQUERY = "/advquery";
	//查询指定条件下总数
	public static final String HITCOUNT = "/hitcount";
	
	//定时任务查询，每次取最近10分钟的数据
	public static final String FETCH = "/fetch";
	
	//test
	public static final String TEST = "/test";
	//favicon.ico
	public static final String FAV = "/favicon.ico";
	
	
	//短信报警
	public static final String ALARM = "/alarm";
	
	//修改es配置
	public static final String ES_MODIFY = "/es/modify";
	
	//获取es配置
	public static final String ES_GET = "/es/get";
}
