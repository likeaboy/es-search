package secfox.soc.es.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.ESClient;
import secfox.soc.netty.server.NettyConfig;

public class ShellRunner {
	
	public static final String MODIFY_COMPATIBILITY = "modify_compatibility";
	public static final String MODIFY_ESIP = "modify_esip";
	public static final String MODIFY_THRESHOLD = "modify_threshold";
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	
	private final static Logger logger = LoggerFactory
			.getLogger(ShellRunner.class);

	public void doCommand(String cmd,Map<String,Object> params) throws Exception{
		switch(cmd){
		case MODIFY_COMPATIBILITY:
			modifyCompatibility(params);
			break;
		case MODIFY_ESIP:
			modifyEsIp(params);
			break;
		case MODIFY_THRESHOLD:
			modifyThreshold(params);
			break;
		}
	}
	
	private void modifyThreshold(Map<String,Object> params) throws Exception{
		//sh modify-threshold.sh /opt/es_monitor/config.cfg 75 77
		String esMonitorConfPath = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_MONITOR_CONF);
		String modifyThresholdShell = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_SHELL_MODIFY_THRESHOLD);
		
		run0(modifyThresholdShell,new String[]{
				esMonitorConfPath,
				String.valueOf(params.get(ESModifyConfigParams.PARAM_ALARMLOGFACTOR)),
				String.valueOf(params.get(ESModifyConfigParams.PARAM_DELLOGFACTOR))});
	}
	
	private void modifyEsIp(Map<String,Object> params)throws Exception{
		// /opt/work/search/es/root/ES*/config/elasticsearch.yml
		// //network.host: 10.95.32.29,127.0.0.1
		String modifyEsIpShell = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_SHELL_MODIFY_ESIP);
		String rootPrefix = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_IP_CONF_ROOT);
		String ymlConfig = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_IP_CONF_FILE);
		String esRestartShell = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_SHELL_RESTART);
		String esIp = String.valueOf(params.get(ESModifyConfigParams.PARAM_ESIP));
		//modified ip
		run0(modifyEsIpShell,new String[]{rootPrefix,ymlConfig,esIp});
		//restart
		int debug=0;
		try{
			debug = Integer.parseInt(NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_SHELL_RESTART_DEBUG));
		}catch(Exception e){
			logger.error("",e);
		}
		if(debug == 0)run0(esRestartShell,new String[]{"restart"});
	}
	
	/*public static void main(String[] args) throws ParseException {
		Date today = sdf.parse(sdf.format(new Date()));
		String t = "skyeye-las_event-2017.12.07";
		String tmp = t.substring(t.length()-10);
		
		if(sdf.parse(tmp).after(today)){
			System.out.println(tmp);
		}
	}*/
	
	private void modifyCompatibility(Map<String,Object> params) throws Exception{
		String esConfMapping = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_MAPPING_ADDR);
		String modifyCompatibilityShell = NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_SHELL_MODIFY_MAPPING);
		//default
		String enabled = "false";
		try{
			int isCompati = Integer.parseInt(String.valueOf(params.get(ESModifyConfigParams.PARAM_ISCOMPATIBILITY)));
			if(isCompati == 1)enabled = "true";
		}catch(Exception e){
		}
		//modify mapping
		run0(modifyCompatibilityShell,new String[]{esConfMapping,enabled});
		
		StringBuilder delSetBuilder = new StringBuilder("[ remove set : ");
		Set<String> indexes = getAllIndices();
		Set<String> wiilRemoved = new HashSet<String>();
		String tmp = "";
		
		Date today = sdf.parse(sdf.format(new Date()));
		for(String index : indexes){
			if(index.startsWith("skyeye-las_event-")){
				//skyeye-las_event-2017.12.05
				tmp = index.substring(index.length()-10);
				if(sdf.parse(tmp).after(today)){
					wiilRemoved.add(index);
					delSetBuilder.append(index);
					delSetBuilder.append(",");
				}
			}
		}
		delSetBuilder.append("]");
		logger.info(delSetBuilder.toString());
		
		//remove index
		//最好检查下doc_count是否为空
		//TODO
		for(String e : wiilRemoved){
			DeleteIndexResponse dResponse = ESClient.getInstance().getClient().admin().indices().prepareDelete(e).execute().actionGet();
			 if (dResponse.isAcknowledged()) {
				 logger.info("[delele index "+e+" success]");
                }else{
                	logger.info("[delete index "+e+" fail]");
                }
		}
		//执行sh /opt/work/search/es/bin/index.sh template.update（修改模板）
		run0(NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_SHELL_INDEX),new String[]{"template.update"});
		
		//执行sh /opt/work/search/es/bin/index.sh -i next create.all（创建未来的index）
		run0(NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_ESCONF_SHELL_INDEX),new String[]{"-i","next","create.all"});
	}
	
	public Set<String> getAllIndices() {
	    ActionFuture<IndicesStatsResponse> isr = ESClient.getInstance().getClient().admin().indices().stats(new IndicesStatsRequest().all());
	    IndicesAdminClient indicesAdminClient = ESClient.getInstance().getClient().admin().indices();
	    Map<String, IndexStats> indexStatsMap = isr.actionGet().getIndices();
	    Set<String> set = isr.actionGet().getIndices().keySet();
	    return set;
	}
	
	public void run0(String shell,String[] params) throws Exception{
		String cmd = buildCMD(shell,params);
		logger.info("[["+cmd+" ] will be exec]");
		Runtime runTime = Runtime.getRuntime();  
		Process process = runTime.exec(cmd);
		process.waitFor();  
	}
	
	public List<String> run1(String[] cmd) throws Exception{
		Runtime runTime = Runtime.getRuntime();  
		Process process = runTime.exec(cmd);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		List<String> rst = new ArrayList<String>();
		StringBuilder print = new StringBuilder();
		String line;
		while ((line = in.readLine()) != null) {
			rst.add(line);
			print.append(line+" ");
		}
		in.close();
		process.waitFor();
		logger.info("[rst : "+print.toString()+"]");
		
		return rst;
	}
	
	public List<String> run1(String cmd) throws Exception{
		logger.info("[["+cmd+" ] will be exec]");
		Runtime runTime = Runtime.getRuntime();  
		Process process = runTime.exec(cmd);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		List<String> rst = new ArrayList<String>();
		StringBuilder print = new StringBuilder();
		String line;
		while ((line = in.readLine()) != null) {
			rst.add(line);
			print.append(line+" ");
		}
		in.close();
		process.waitFor();
		logger.info("[rst : "+print.toString()+"]");
		
		return rst;
	}
	
	private String buildCMD(String shell,String[] params){
		StringBuilder cmdBuilder = new StringBuilder("sh ");
		cmdBuilder.append(shell);
		if(params != null){
			cmdBuilder.append(" ");
			cmdBuilder.append(appendParams(params));
		}
		return cmdBuilder.toString();
	}
	
	private String appendParams(String[] params){
		StringBuilder builder = new StringBuilder();
		for(String param : params){
			builder.append(param);
			builder.append(" ");
		}
		return builder.toString().trim();
	}
}
