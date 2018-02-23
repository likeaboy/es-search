package secfox.soc.netty.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件
 * @author wangzhijie
 *
 */
public class NettyConfig 
{
	private final static Logger logger = LoggerFactory.getLogger(NettyConfig.class);
	private static volatile NettyConfig instance = new NettyConfig();
	
	private Properties prop = new Properties();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	
	public String getPropertyFilePath(String fname) throws IOException
    {
        String jarWholePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
        String jarPath = new File(jarWholePath).getParentFile().getAbsolutePath()+"/"+fname;
        return jarPath;
    }
	
	public void show(){
	 logger.info("show load netty.conf value:");
	 logger.info(KEY_SEARCH_PAGESIZE+"="+prop.getProperty(KEY_SEARCH_PAGESIZE));
	 logger.info("show end...");
	}
	
	private NettyConfig(){
        try{
            InputStream in = new FileInputStream(new File(getPropertyFilePath("netty.conf")));
            prop.load(in);
            
            show();
            
            in.close();
        }
        catch(Exception e){
        	logger.error("",e);
        }
	}
	/**
	 * 动态生成es index,规则：idx-yyyy.MM.dd
	 * @param topic
	 * @return
	 */
	public String generateEsIdx(String index){
		StringBuilder idx = new StringBuilder(index);
		idx.append("-");
		idx.append(sdf.format(new Date()));
		return idx.toString();
	}
	
	public void refreshIndex(String newIdx){
		prop.setProperty(KEY_ES_IDX, newIdx);
	}
	
	public static NettyConfig getInstance(){
		return instance;
	}
	
	public String getConfigValue(String configKey){
		return (String)prop.get(configKey);
	}
	
	public boolean isQueryDebug(){
		int debug = Integer.parseInt((String)prop.get(KEY_ES_QUERY_DEBUG));
		if(debug == 1)return true;
		return false;
	}
	
	public static final String KEY_ES_CLUSTERNAME = "es.cluster.name";
	public static final String KEY_ES_HOST = "es.host";
	public static final String KEY_ES_PORT = "es.port";
	public static final String KEY_ES_IDX = "es.index";
	public static final String KEY_ES_TYPE = "es.type";
	public static final String KEY_ES_QUERY_DEBUG = "es.query.debug";
	public static final String KEY_SERVER_HOST = "server.host";
	public static final String KEY_SERVER_PORT = "server.port";
	public static final String KEY_SEARCH_PAGESIZE = "search.pagesize";
	public static final String KEY_PYSHELL_PATH = "alarm.pyshell.path";
	public static final String KEY_PYSHELL_FUNCTION = "alarm.pyshell.function";
	
	public static final String KEY_PYSHELL_PARAMS_PATH = "alarm.pyshell.params.path";
	public static final String KEY_PYSHELL_RESULT_PATH = "alarm.pyshell.result.path";
	
	
	public static final String KEY_ESCONF_MAPPING_ADDR = "es.conf.mapping.addr";
	public static final String KEY_ESCONF_MONITOR_CONF = "es.conf.monitor.conf";
	
	public static final String KEY_ESCONF_SHELL_MODIFY_MAPPING = "es.conf.shell.modify.mapping";
	public static final String KEY_ESCONF_SHELL_MODIFY_THRESHOLD = "es.conf.shell.modify.threshold";
	public static final String KEY_ESCONF_SHELL_MODIFY_ESIP = "es.conf.shell.modify.esip";
	public static final String KEY_ESCONF_SHELL_RESTART = "es.conf.shell.restart";
	public static final String KEY_ESCONF_SHELL_READ_ESIP = "es.conf.shell.read.esip";
	public static final String KEY_ESCONF_SHELL_READ_ESCOMPATI = "es.conf.shell.read.escompati";
	public static final String KEY_ESCONF_SHELL_INDEX = "es.conf.shell.index";
	
	//0--开启restart，1--关闭restart
	public static final String KEY_ESCONF_SHELL_RESTART_DEBUG = "es.conf.shell.restart.debug";
	
	public static final String KEY_ESCONF_IP_CONF_ROOT = "es.conf.ip.conf.root";
	public static final String KEY_ESCONF_IP_CONF_FILE = "es.conf.ip.conf.file";
	
	//mysql
	public static final String KEY_MYSQL_HOST = "mysql.host";
	public static final String KEY_MYSQL_PORT = "mysql.port";
	public static final String KEY_MYSQL_USER = "mysql.user";
	public static final String KEY_MYSQL_PWD = "mysql.pwd";
	
	
}
