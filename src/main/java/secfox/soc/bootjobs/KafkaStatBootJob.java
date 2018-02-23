package secfox.soc.bootjobs;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.netty.server.bootjobs.IBootJob;

/**
 * kafka 统计
 * @author wangzhijie
 *
 */
public class KafkaStatBootJob extends TimerTask implements IBootJob{
	
	private final static Logger logger = LoggerFactory
			.getLogger(KafkaStatBootJob.class);
	
	public static final int SCHEDULE_CYCLE = 30 * 1000;

	@Override
	public void start() {
		// TODO Auto-generated method stub
		try {
			logger.info("[KafkaStatCron start,schedule cycle : 30s]");
			//启动后台线程30s定时check
			Timer timer = new Timer(); 
			timer.schedule(new KafkaAliveCheckerBootJob(), SCHEDULE_CYCLE, SCHEDULE_CYCLE); 
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//kafka stat v3
		
	}
	
}
