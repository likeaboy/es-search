package secfox.soc.bootjobs;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.common.ShellRunner;
import secfox.soc.netty.server.bootjobs.IBootJob;

public class KafkaAliveCheckerBootJob extends TimerTask implements IBootJob{
	
	private final static Logger logger = LoggerFactory.getLogger(KafkaAliveCheckerBootJob.class);
	
	private ShellRunner shell = new ShellRunner();
	
	public static final int SCHEDULE_CYCLE = 30 * 1000;

	@Override
	public void start() {
		try {
			logger.info("[KafkaAliveCheckerBootJob start,schedule cycle : 30s]");
			//启动后台线程30s定时check
			Timer timer = new Timer(); 
			timer.schedule(new KafkaAliveCheckerBootJob(), SCHEDULE_CYCLE, SCHEDULE_CYCLE); 
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	@Override
	public void run() {
		try {
			String kafkaPid = null;
//			"/bin/sh -c cd / && echo $(jps | grep Kafka)"
			List<String> rst = shell.run1(new String[]{"/bin/sh","-c","jps | grep Kafka"});
			if(rst != null && rst.size() != 0){
				kafkaPid = rst.get(0);
			}
			logger.info("[do kafka alive check,kafka pid={}]",kafkaPid);
			if(StringUtils.isEmpty(kafkaPid)){
				//call killer checker
				List<String> killerRst = shell.run1("sh checker-killer.sh");
				StringBuilder killerRstAppender = new StringBuilder("[call checker-killer.sh , result : ");
				for(String e : killerRst)
					killerRstAppender.append(e+",");
				killerRstAppender.append("]");
				logger.info(killerRstAppender.toString());
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}
}
