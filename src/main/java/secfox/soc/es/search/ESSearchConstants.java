package secfox.soc.es.search;

public class ESSearchConstants {
	/**
	 * recept_time(date) -- receptTime 接收时间
	 * dev--devAddr 设备地址
	 * sip -- sAddr 源地址
	 *  sport -- sPort 源端口
	 * dip--dAddr 目的地址
	 * dport -- dPort 目的端口
	 * id -- ID 事件ID
	 * system_type -- sysType 系统类型
	 * pri -- priority  等级
	 * dev_type -- devType 设备类型
	 * act -- action 操作
	 * ret -- result 结果
	 * 
	 * user -- sUserName 源用户名称
	 * nat_sip (ip)-- stAddr  源地址转换
	 * nat_sport (int)-- stPort   源地址转换端口
	 * nat_dip (ip) -- dtAddr 目的地址转换
	 * nat_dport(int) -- dtPort 目的地址转换端口
	 * 
	 * msg -- ESWords 关键字
	 */
	public static final String ES_RECEPT_TIME = "recept_time";
	public static final String ES_DEV = "dev";
	public static final String ES_SIP = "sip";
	public static final String ES_SPORT = "sport";
	public static final String ES_DIP = "dip";
	public static final String ES_DPORT = "dport";
	//add new 5 fields
	public static final String ES_USER = "user";
	public static final String ES_NAT_SIP = "nat_sip";
	public static final String ES_NAT_SPORT = "nat_sport";
	public static final String ES_NAT_DIP = "nat_dip";
	public static final String ES_NAT_DPORT = "nat_dport";
	
	public static final String ES_ID = "id";
	public static final String ES_SYSTEM_TYPE = "system_type";
	public static final String ES_PRIORITY = "pri";
	public static final String ES_DEV_TYPE = "dev_type";
	public static final String ES_DEV_CATEGORY = "dev_category";
	
	public static final String ES_ACT = "act";
	public static final String ES_RET = "ret";
	
	//fuzzy 
	public static final String ES_MSG = "msg";
	
	//dev_vendor
	public static final String ES_DEV_VENDOR="dev_vendor";
	//dev_product
	public static final String ES_DEV_PRODUCT="dev_product";
	//session
	public static final String ES_SESSION="session";
	//uri
	public static final String ES_URI="uri";
	//custom_s1
	public static final String ES_CUSTOMS1="custom_s1";
	public static final String ES_CUSTOMS2="custom_s2";
	public static final String ES_CUSTOMS3="custom_s3";
	public static final String ES_CUSTOMS4="custom_s4";
	
	//resource
	public static final String ES_RESOURCE="resource";
	//custom_d1
	public static final String ES_CUSTOMD1="custom_d1";
	public static final String ES_CUSTOMD2="custom_d2";
	public static final String ES_CUSTOMD3="custom_d3";
	public static final String ES_CUSTOMD4="custom_d4";
	
	//smac
	public static final String ES_SMAC="smac";
	//dmac
	public static final String ES_DMAC="dmac";
	
	//raw_id
	public static final String ES_RAWID="raw_id";
	
	//collector_type
	public static final String ES_COLLECTOR_TYPE="collector_type";
	
	//name
	public static final String ES_NAME="name";
	
	//object
	public static final String ES_OBJECT="object";
}
