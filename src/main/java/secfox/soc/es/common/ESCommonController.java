package secfox.soc.es.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.ESClient;
import secfox.soc.netty.IBaseResponse;
import secfox.soc.netty.server.NettyConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * ES配置
 * @author wangzhijie
 *
 */
public class ESCommonController {
	private final static Logger logger = LoggerFactory
			.getLogger(ESCommonController.class);

	private static volatile ESCommonController instance = new ESCommonController();
	
	private ShellRunner shell = new ShellRunner();
	
	private ESModifyConfigFilter filter = new ESModifyConfigFilter();
	
	private ESCommonController() {
	}
	
	public static ESCommonController getInstance(){
		return instance;
	}
	
	public IBaseResponse modify(Map<String,Object> params){
		ESModifyResponse resp = new ESModifyResponse();
		resp.setCode(ESModifyResponse.Type.OK.code);
		resp.setMsg(ESModifyResponse.Type.OK.msg);
		//check params
		ESModifyResponse filterResp = filter.check(params);
		if(filterResp.getCode() != ESModifyResponse.Type.OK.code)
			return filterResp;
		
		try {
			if(params.containsKey(ESModifyConfigParams.PARAM_ISCOMPATIBILITY)){
				shell.doCommand(ShellRunner.MODIFY_COMPATIBILITY,params);
			}
			
			if(params.containsKey(ESModifyConfigParams.PARAM_DELLOGFACTOR) && 
					params.containsKey(ESModifyConfigParams.PARAM_ALARMLOGFACTOR)){
				shell.doCommand(ShellRunner.MODIFY_THRESHOLD,params);
			}
			
			if(params.containsKey(ESModifyConfigParams.PARAM_ESIP)){
				shell.doCommand(ShellRunner.MODIFY_ESIP,params);
			}
		}catch(Exception e){
			logger.error("",e);
			resp.setCode(ESModifyResponse.Type.ERROR.code);
			resp.setMsg(ESModifyResponse.Type.ERROR.msg);
		}
		
		return resp;
	}
	
	public IBaseResponse get(){
		ESGetResponse resp = new ESGetResponse();
		resp.setCode(ESGetResponse.Type.OK.code);
		resp.setMsg(ESGetResponse.Type.OK.msg);
		try {
			//read es monitor conf
			readMonitorConf(resp);
		
			readCompatibility(resp);
			
			//read ip
			readCurrentEsIp(resp);
		} catch (Exception e) {
			logger.error("",e);
			resp.setCode(ESGetResponse.Type.ERROR.code);
			resp.setMsg(ESGetResponse.Type.ERROR.msg);
		}
		return resp;
	}
	
	private void readCurrentEsIp(ESGetResponse resp) throws Exception{
		String readEsip = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_SHELL_READ_ESIP);
		StringBuilder cmd = new StringBuilder("sh ");
		cmd.append(readEsip);
		List<String> rst = shell.run1(cmd.toString());
		String networkHost = rst.get(0);
		try{
			String host = (networkHost.split(":")[1]).split(",")[0];
			resp.setEsIp(host);
			logger.info("[read esip: "+host+"]");
		}catch(Exception e){
			logger.error("",e);
			NodesInfoResponse nodeInfoResp = ESClient.getInstance().getClient().admin().cluster().prepareNodesInfo().execute().actionGet();
	        
	        List<NodeInfo> nodes = nodeInfoResp.getNodes();
	        
	        for(NodeInfo nodeInfo : nodes){
	            DiscoveryNode node = nodeInfo.getNode();
	            resp.setEsIp(node.getHostAddress());
	            logger.info("[read esip: "+node.getHostAddress()+"]");
	            break;
	        }
		}
	}
	
	private void readMonitorConf(ESGetResponse resp) throws IOException{
		String esMonitorConfPath = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_MONITOR_CONF);
		Properties prop = new Properties();
		 InputStream in = new FileInputStream(new File(esMonitorConfPath));
         prop.load(in);
         in.close();
         String alarmFactor = (String)prop.get("alarm_threshold");
  		String clearFactor = (String)prop.get("clear_threshold");
 		
  		logger.info("[read alarm_threshold : "+alarmFactor+",clear_threshold : "+clearFactor+"]");
  		
 		resp.setAlarmLogFactor(alarmFactor);
 		resp.setDelLogFactor(clearFactor);
		
	}
	
	private void readCompatibility(ESGetResponse resp) throws Exception{
		String readCompatiShell = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_SHELL_READ_ESCOMPATI);
		StringBuilder cmd = new StringBuilder("sh ");
		cmd.append(readCompatiShell);
		List<String> rst = shell.run1(cmd.toString());
		
		JSONObject _all = JSON.parseObject("{"+rst.get(0)+"}");
		boolean isCompatibility = _all.getBoolean("enabled");
		logger.info("[read compatibility: "+_all.toString()+"]");
		resp.setIsCompatibility(isCompatibility == true ? 1 : 0);
	}
	
	/*public static void main(String[] args) {
		String t = "{\"enabled\": false}";
		JSONObject o = JSON.parseObject(t);
		boolean isCompatibility = o.getBoolean("enabled");
		System.out.println(isCompatibility);
		
	}*/
		
}
