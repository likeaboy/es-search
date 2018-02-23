package secfox.soc.es.search.request;

public class ESQueryRequest {
	
	private String requestUrl;
	private boolean fetchOnlyOnce=false;
	
	private String keyWords;
	private long startTime;
	private long endTime;
	private String devAddr;
	private String sAddr;
	private int sPort;
	private String dAddr;
	private int dPort;
	private String id;
	private String sysType;
	private String priority;
	private String devType;
	private String action;
	private String result;
	private String sUserName;
	private String stAddr;
	private int stPort;
	private String dtAddr;
	private int dtPort;
	
	//second add
	private String devVendor;
	private String devProduct;
	private String sessionID;
	private String requestURI;
	private String customS1;
	private String customS2;
	private String customS3;
	private String customS4;
	private String resource;
	private String customD1;
	private String customD2;
	private String customD3;
	private String customD4;
	private String sMAC;
	private String dMAC;
	private String rawID;
	private String collectType;
	private String name;
	private String object;
	
	//asc,desc
	private String order;
	//page size, default 1000
	private int pageSize = 1000;
	private String hitIndex;
	private String hitSlotKey;
	private int slotFrom;
	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getDevAddr() {
		return devAddr;
	}
	public void setDevAddr(String devAddr) {
		this.devAddr = devAddr;
	}
	public String getsAddr() {
		return sAddr;
	}
	public void setsAddr(String sAddr) {
		this.sAddr = sAddr;
	}
	public int getsPort() {
		return sPort;
	}
	public void setsPort(int sPort) {
		this.sPort = sPort;
	}
	public String getdAddr() {
		return dAddr;
	}
	public void setdAddr(String dAddr) {
		this.dAddr = dAddr;
	}
	public int getdPort() {
		return dPort;
	}
	public void setdPort(int dPort) {
		this.dPort = dPort;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSysType() {
		return sysType;
	}
	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getDevType() {
		return devType;
	}
	public void setDevType(String devType) {
		this.devType = devType;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getsUserName() {
		return sUserName;
	}
	public void setsUserName(String sUserName) {
		this.sUserName = sUserName;
	}
	public String getStAddr() {
		return stAddr;
	}
	public void setStAddr(String stAddr) {
		this.stAddr = stAddr;
	}
	public int getStPort() {
		return stPort;
	}
	public void setStPort(int stPort) {
		this.stPort = stPort;
	}
	public String getDtAddr() {
		return dtAddr;
	}
	public void setDtAddr(String dtAddr) {
		this.dtAddr = dtAddr;
	}
	public int getDtPort() {
		return dtPort;
	}
	public void setDtPort(int dtPort) {
		this.dtPort = dtPort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
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
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public boolean isFetchOnlyOnce() {
		return fetchOnlyOnce;
	}
	public void setFetchOnlyOnce(boolean fetchOnlyOnce) {
		this.fetchOnlyOnce = fetchOnlyOnce;
	}
	public String getDevVendor() {
		return devVendor;
	}
	public void setDevVendor(String devVendor) {
		this.devVendor = devVendor;
	}
	public String getDevProduct() {
		return devProduct;
	}
	public void setDevProduct(String devProduct) {
		this.devProduct = devProduct;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getRequestURI() {
		return requestURI;
	}
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}
	public String getCustomS1() {
		return customS1;
	}
	public void setCustomS1(String customS1) {
		this.customS1 = customS1;
	}
	public String getCustomS2() {
		return customS2;
	}
	public void setCustomS2(String customS2) {
		this.customS2 = customS2;
	}
	public String getCustomS3() {
		return customS3;
	}
	public void setCustomS3(String customS3) {
		this.customS3 = customS3;
	}
	public String getCustomS4() {
		return customS4;
	}
	public void setCustomS4(String customS4) {
		this.customS4 = customS4;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getCustomD1() {
		return customD1;
	}
	public void setCustomD1(String customD1) {
		this.customD1 = customD1;
	}
	public String getCustomD2() {
		return customD2;
	}
	public void setCustomD2(String customD2) {
		this.customD2 = customD2;
	}
	public String getCustomD3() {
		return customD3;
	}
	public void setCustomD3(String customD3) {
		this.customD3 = customD3;
	}
	public String getCustomD4() {
		return customD4;
	}
	public void setCustomD4(String customD4) {
		this.customD4 = customD4;
	}
	public String getsMAC() {
		return sMAC;
	}
	public void setsMAC(String sMAC) {
		this.sMAC = sMAC;
	}
	public String getdMAC() {
		return dMAC;
	}
	public void setdMAC(String dMAC) {
		this.dMAC = dMAC;
	}
	public String getRawID() {
		return rawID;
	}
	public void setRawID(String rawID) {
		this.rawID = rawID;
	}
	public String getCollectType() {
		return collectType;
	}
	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
}
