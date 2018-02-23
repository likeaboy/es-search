package secfox.soc.bootjobs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.netty.server.NettyConfig;
import secfox.soc.netty.server.bootjobs.IBootJob;
import secfox.soc.util.SQLUtils;

public class KafkaStatRecordCleanBootJob extends TimerTask implements IBootJob {

	private final static Logger logger = LoggerFactory
			.getLogger(KafkaStatRecordCleanBootJob.class);
	
	private final int SCHEDULE_CYCLE = 24 * 60 * 60 * 1000;

	@Override
	public void start() {
		try {
			//每天凌晨1点执行
			Calendar calendar = Calendar.getInstance();  
	        calendar.set(Calendar.HOUR_OF_DAY, 1); //凌晨1点  
	        calendar.set(Calendar.MINUTE, 0);  
	        calendar.set(Calendar.SECOND, 0);  
	        Date date=calendar.getTime(); 
	        if (date.before(new Date())) {  
	            date = this.addDay(date, 1);  
	        }  
			logger.info("[KafkaStatRecordCleanBootJob start,schedule cycle : 1 days]");
			 Timer timer = new Timer();  
		        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。  
		        timer.schedule(new KafkaStatRecordCleanBootJob(),date,
						SCHEDULE_CYCLE);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static void main(String[] args) {
		new KafkaStatRecordCleanBootJob().run();
	}
	
	 // 增加或减少天数  
    public Date addDay(Date date, int num) {  
        Calendar startDT = Calendar.getInstance();  
        startDT.setTime(date);  
        startDT.add(Calendar.DAY_OF_MONTH, num);  
        return startDT.getTime();  
    }  

	@Override
	public void run() {
		String mysqlHost = NettyConfig.getInstance().getConfigValue(
				NettyConfig.KEY_MYSQL_HOST);
		String mysqlPort = NettyConfig.getInstance().getConfigValue(
				NettyConfig.KEY_MYSQL_PORT);
		String mysqlUser = NettyConfig.getInstance().getConfigValue(
				NettyConfig.KEY_MYSQL_USER);
		String mysqlPwd = NettyConfig.getInstance().getConfigValue(
				NettyConfig.KEY_MYSQL_PWD);
		logger.info("[do kafka stat record clean...]");
		try {
			Connection conn = SQLUtils.createConn(mysqlHost, mysqlPort,
					mysqlUser, mysqlPwd);
			ResultSet rs = SQLUtils
					.executeQuerySQL(conn,
							"select * from soc.stat_kafka_seconds order by time desc limit 10;");
			String latestTime = null;
			while (rs.next()) {
				latestTime = rs.getString("time");
				if (StringUtils.isNotEmpty(latestTime))
					break;
			}

			Calendar c = Calendar.getInstance();
			c.setTime(new Date(Long.parseLong(latestTime)));
			c.add(Calendar.DAY_OF_YEAR, -30);
			long delBoundary = c.getTime().getTime();
			logger.info("[latestTime : {}, delete before {}]", latestTime,
					delBoundary);

			long deleteCount = SQLUtils.executeDelSQL(conn,
					"delete from soc.stat_kafka_seconds where time < '"
							+ delBoundary + "'");

			logger.info("[DELETE : {}]", deleteCount);
		} catch (Exception e) {
			System.out.println(e);
			logger.error("MYSQL ERROR:", e);
		}
	}

}
