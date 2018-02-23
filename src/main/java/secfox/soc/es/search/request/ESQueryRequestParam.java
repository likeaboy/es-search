package secfox.soc.es.search.request;

import secfox.soc.netty.server.NettyConfig;

public class ESQueryRequestParam {
	//模糊查询使用
	public static final String KEY_KEYWORDS="keyWords";

	public static final String KEY_STARTTIME="startTime";
	public static final String KEY_ENDTIME="endTime";
	public static final String KEY_DEVADDR="devAddr";
	public static final String KEY_SADDR="sAddr";
	public static final String KEY_SPORT="sPort";
	public static final String KEY_DADDR="dAddr";
	public static final String KEY_DPORT="dPort";
	public static final String KEY_ID="id";
	public static final String KEY_SYSTYPE="sysType";
	public static final String KEY_PRIORITY="priority";
	public static final String KEY_DEVTYPE="devType";
	public static final String KEY_DEVCATEGORY="devCategory";
	public static final String KEY_ACTION="action";
	public static final String KEY_RESULT="result";
	
	public static final String KEY_SUSERNAME="sUserName";
	public static final String KEY_STADDR="stAddr";
	public static final String KEY_STPORT="stPort";
	public static final String KEY_DTADDR="dtAddr";
	public static final String KEY_DTPORT="dtPort";
	
	//dev_vendor
	public static final String KEY_DEV_VENDOR="devVendor";
	//dev_product
	public static final String KEY_DEV_PRODUCT="devProduct";
	//session
	public static final String KEY_SESSION="sessionID";
	//uri
	public static final String KEY_REQUESTURI="requestURI";
	//custom_s1
	public static final String KEY_CUSTOMS1="customS1";
	public static final String KEY_CUSTOMS2="customS2";
	public static final String KEY_CUSTOMS3="customS3";
	public static final String KEY_CUSTOMS4="customS4";
	
	//resource
	public static final String KEY_RESOURCE="resource";
	//custom_d1
	public static final String KEY_CUSTOMD1="customD1";
	public static final String KEY_CUSTOMD2="customD2";
	public static final String KEY_CUSTOMD3="customD3";
	public static final String KEY_CUSTOMD4="customD4";
	
	//smac
	public static final String KEY_SMAC="sMAC";
	//dmac
	public static final String KEY_DMAC="dMAC";
	
	//raw_id
	public static final String KEY_RAWID="rawID";
	
	//collector_type
	public static final String KEY_COLLECT_TYPE="collectType";
	
	//name
	public static final String KEY_NAME="name";
	
	//object
	public static final String KEY_OBJECT="object";
	
	//asc,desc
	public static final String KEY_ORDER="order"; 
	//page size, default 50
	public static final String KEY_PAGE_SIZE="pageSize"; 
	
	public static final String KEY_HIT_INDEX="hitIndex"; 
	public static final String KEY_HIT_SLOTKEY="hitSlotKey";
	public static final String KEY_SLOT_FROM="slotFrom";
	
	public static class TimeOrder {
		public static final String ASC="asc"; 
		public static final String DESC="desc"; 
	}
	
	public static final int FETCH_PAGESIZE = Integer.parseInt(NettyConfig.getInstance()
			.getConfigValue(NettyConfig.KEY_SEARCH_PAGESIZE));
}
