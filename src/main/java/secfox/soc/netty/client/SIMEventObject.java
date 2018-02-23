/**
 * 版权所有 ( c ) 北京网御神州科技有限公司 2006。保留所有权利。
 *
 * 项目：	     manageserver
 * 文件名：	【SIMEventObject.java】
 * 描述：	    【管理服务器-通用数据结构-事件部分-事件对像】
 * 作者名：	【朱震】
 * 日期：	    【2006-06-07】
 * $Id: SIMEventObject.java,v 1.7.2.1.2.8 2014-06-19 03:50:15 zhaoff Exp $
 * 修改历史：
 * 【时间】		【修改者】	【修改内容】
 * 20060619      文华         增加了字段
 */
package secfox.soc.netty.client;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 归一化后的事件对像。
 * 
 * @author zhuzhen
 * 
 */

public class SIMEventObject implements Serializable, Cloneable {

	private static final long serialVersionUID = 3181284370613758274L;

	/**
	 *  2012-8-1 LAS6.0精简字段后的事件对象，共53个。
	 *  针对新版事件属性字段，地址类字段在传输中都是字符形式，即“10.1.1.1”与“fe80::2”形式，只有在涉及存储数据库的时候才转换为byte[]形式
	 */
	@JSONField(name = "id")
	public long ID = 0; // 事件ID，ID=0表示尚未归一化。

	@JSONField(name = "recept_time")
	public long receptTime; // 事件接收时刻

	@JSONField(name = "agg_count")
	public long aggregatedCount;// 聚合事件数 [new]

	@JSONField(name = "system_type")
	public int sysType; // 系统类型: 1:系统普通事件；2: 系统告警事件；3:外部事件

	@JSONField(name = "collector_ip")
	public String collectorAddr = " "; // 采集器地址 [new]

	@JSONField(name = "collector_name")
	public String collectorName = " "; // 采集器名称 [new]

	@JSONField(name = "collector_type")
	public int collectType; // 获取方式: 1：syslog；2：snmp trap；3：file；4：jdbc

	@JSONField(name = "name")
	public String name = " ";// 事件简短描述

	@JSONField(name = "pt")
	public Integer category=0; // 事件分类

	@JSONField(name = "systype")
	public Integer type=0; // 事件具体类型，由日志本身决定

	@JSONField(name = "pri")
	public Integer priority = null; // 等级:0,1,2,3,4 , 4最严重.

	@JSONField(name = "raw_id")
	public Long rawID = 0l; // 事件分类号: 事件本身的编号,由产生事件的系统决定.

	@JSONField(name = "occur_time")
	public Long occurTime = null; // 事件产生时刻: 事件本身纪录的时间

	@JSONField(name = "duration")
	public Long duration = null; // 事件持续时刻：

	@JSONField(name = "uplink_length")
	public Long send = new Long(0); // 发送字节数：为其赋初值为0，以防止统计流量报表时出错

	@JSONField(name = "downlink_length")
	public Long receive = new Long(0); // 接收字节数：为其赋初值为0，以防止统计流量报表时出错

	@JSONField(name = "proto")
	public Integer protocol = null; // [协议]: tcp,

	@JSONField(name = "app_protocol")
	public Integer appProtocol = null; // [应用协议]：http,https,ftp,

	@JSONField(name = "object")
	public Integer object; // 对象: 作用于谁

	@JSONField(name = "pol")
	public Integer policy; // 策略: 使用的方法, [比如攻击的方法,病毒的方法]

	@JSONField(name = "resource")
	public String resource = " "; // 资源名称: 体的资源:文件或目录或进程或连接或外接设备

	@JSONField(name = "act")
	public Integer action = 0; // 操作: 启动,停止,连接,访问,增,删,改,查,授权,登陆

	@JSONField(name = "rule_name")
	public String intent = " "; // 意图：危害，敌意，信息，正常... [new]

	@JSONField(name = "ret")
	public Integer result=0; // 结果: 成功,失败,尝试

	@JSONField(name = "sip")
	public String sAddr = " "; // 源地址

	@JSONField(name = "sport")
	public Integer sPort = null; // 源端口

	@JSONField(name = "user")
	public String sUserName = " "; // 源用户名称

	@JSONField(name = "nat_sip")
	public String stAddr = " "; // 源地址转换

	@JSONField(name = "nat_sport")
	public Integer stPort = null; // 源地址转换端口

	@JSONField(name = "dip")
	public String dAddr = " "; // 目的地址

	@JSONField(name = "dport")
	public Integer dPort = null; // 目的端口

	@JSONField(name = "d_user")
	public String dUserName = " "; // 目的用户名称

	@JSONField(name = "nat_dip")
	public String dtAddr = " "; // 目的地址转换

	@JSONField(name = "nat_dport")
	public Integer dtPort = null; // 目的地址转换端口

	@JSONField(name = "dev")
	public String devAddr = " "; // 设备地址

	@JSONField(name = "dev_name")
	public String devName = " "; // 设备名称

	@JSONField(name = "dev_category")
	public Integer devCategory = null; // 设备分类

	@JSONField(name = "dev_type")
	public Integer devType=0; // 设备型号

	@JSONField(name = "dev_vendor")
	public String devVendor = " "; // 设备厂商

	@JSONField(name = "dev_product")
	public String devProduct = " "; // 设备产品 20070809 zhuzhen

	@JSONField(name = "dproc")
	public String programType = " "; // “程序类型”改为“业务名称”

	@JSONField(name = "session")
	public String sessionID = " "; // 会话ID 20070809 zhuzhen

	@JSONField(name = "uri")
	public String requestURI = " "; // 请求URI [new]

	@JSONField(name = "msg")
	public String msg = " "; // 内容: 描述信息，一般填写原始日志信息

	@JSONField(name = "custom_s1")
	public String customS1 = " "; // 备用字符串1 [new]

	@JSONField(name = "custom_s2")
	public String customS2 = " "; // 备用字符串2 [new]

	@JSONField(name = "custom_s3")
	public String customS3 = " "; // 备用字符串3 [new]

	@JSONField(name = "custom_s4")
	public String customS4 = " "; // 备用字符串4 [new]

	@JSONField(name = "smac")
	public String sMAC = " "; // 源MAC地址 [new] 20070809 zhuzhen

	@JSONField(name = "dmac")
	public String dMAC = " "; // 目的MAC地址 [new] 20070809 zhuzhen

	@JSONField(name = "custom_d1")
	public Long customD1 = null; // 备用数字1 [new]

	@JSONField(name = "custom_d2")
	public Long customD2 = null; // 备用数字2 [new]

	@JSONField(name = "custom_d3")
	public Double customD3 = null; // 备用数字3 [new] 20070809 zhuzhen

	@JSONField(name = "custom_d4")
	public Double customD4 = null; // 备用数字4 [new] 20070809 zhuzhen

	@JSONField(serialize = false)
	public Long position=-1l;
	
	public long getID() {
		return ID;
	}

	public void setID(long id) {
		ID = id;
	}

	public long getReceptTime() {
		return receptTime;
	}

	public void setReceptTime(long receptTime) {
		this.receptTime = receptTime;
	}

	public long getAggregatedCount() {
		return aggregatedCount;
	}

	public void setAggregatedCount(long aggregatedCount) {
		this.aggregatedCount = aggregatedCount;
	}

	public int getSysType() {
		return sysType;
	}

	public void setSysType(int sysType) {
		this.sysType = sysType;
	}

	public String getCollectorName() {
		return collectorName;
	}

	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}

	public int getCollectType() {
		return collectType;
	}

	public void setCollectType(int collectType) {
		this.collectType = collectType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getRawID() {
		return rawID;
	}

	public void setRawID(Long rawID) {
		this.rawID = rawID;
	}

	public Long getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(Long occurTime) {
		this.occurTime = occurTime;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Long getSend() {
		return send;
	}

	public void setSend(Long send) {
		this.send = send;
	}

	public Long getReceive() {
		return receive;
	}

	public void setReceive(Long receive) {
		this.receive = receive;
	}

	public Integer getProtocol() {
		return protocol;
	}

	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}

	public Integer getAppProtocol() {
		return appProtocol;
	}

	public void setAppProtocol(Integer appProtocol) {
		this.appProtocol = appProtocol;
	}

	public Integer getObject() {
		return object;
	}

	public void setObject(int object) {
		this.object = object;
	}

	public Integer getPolicy() {
		return policy;
	}

	public void setPolicy(int policy) {
		this.policy = policy;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Integer getSPort() {
		return sPort;
	}

	public void setSPort(Integer port) {
		sPort = port;
	}

	public String getSUserName() {
		return sUserName;
	}

	public void setSUserName(String userName) {
		sUserName = userName;
	}

	public Integer getStPort() {
		return stPort;
	}

	public void setStPort(Integer stPort) {
		this.stPort = stPort;
	}

	public Integer getDPort() {
		return dPort;
	}

	public void setDPort(Integer port) {
		dPort = port;
	}

	public String getDUserName() {
		return dUserName;
	}

	public void setDUserName(String userName) {
		dUserName = userName;
	}

	public Integer getDtPort() {
		return dtPort;
	}

	public void setDtPort(Integer dtPort) {
		this.dtPort = dtPort;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public Integer getDevCategory() {
		return devCategory;
	}

	public void setDevCategory(Integer devCategory) {
		this.devCategory = devCategory;
	}

	public int getDevType() {
		return devType;
	}

	public void setDevType(int devType) {
		this.devType = devType;
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

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	public String getSMAC() {
		return sMAC;
	}

	public void setSMAC(String smac) {
		sMAC = smac;
	}

	public String getDMAC() {
		return dMAC;
	}

	public void setDMAC(String dmac) {
		dMAC = dmac;
	}

	public Long getCustomD1() {
		return customD1;
	}

	public void setCustomD1(Long customD1) {
		this.customD1 = customD1;
	}

	public Long getCustomD2() {
		return customD2;
	}

	public void setCustomD2(Long customD2) {
		this.customD2 = customD2;
	}

	public Double getCustomD3() {
		return customD3;
	}

	public void setCustomD3(Double customD3) {
		this.customD3 = customD3;
	}

	public Double getCustomD4() {
		return customD4;
	}

	public void setCustomD4(Double customD4) {
		this.customD4 = customD4;
	}

	public String getCollectorAddr() {
		return collectorAddr;
	}

	public void setCollectorAddr(String collectorAddr) {
		this.collectorAddr = collectorAddr;
	}

	public String getSAddr() {
		return sAddr;
	}

	public void setSAddr(String addr) {
		sAddr = addr;
	}

	public String getStAddr() {
		return stAddr;
	}

	public void setStAddr(String stAddr) {
		this.stAddr = stAddr;
	}

	public String getDAddr() {
		return dAddr;
	}

	public void setDAddr(String addr) {
		dAddr = addr;
	}

	public String getDtAddr() {
		return dtAddr;
	}

	public void setDtAddr(String dtAddr) {
		this.dtAddr = dtAddr;
	}

	public String getDevAddr() {
		return devAddr;
	}

	public void setDevAddr(String devAddr) {
		this.devAddr = devAddr;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Long getPosition() {
		return position;
	}

	public void setPosition(Long position) {
		this.position = position;
	}
}
