package secfox.soc.bootjobs;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.netty.server.bootjobs.IBootJob;
import secfox.soc.netty.server.bootjobs.IBootManager;

public class BootJobManager implements IBootJob,IBootManager{
	
	private final static Logger logger = LoggerFactory
			.getLogger(BootJobManager.class);

	private List<IBootJob> bootJobsRegister = new ArrayList<IBootJob>();
	@Override
	public void register(IBootJob job){
		bootJobsRegister.add(job);
	}
	@Override
	public void remove(IBootJob job){
		bootJobsRegister.remove(job);
	}

	@Override
	public void start() {
		logger.info("[boot job mamanger start...]");
		register(new KafkaAliveCheckerBootJob());
		register(new KafkaStatRecordCleanBootJob());
		// TODO Auto-generated method stub
		for(IBootJob job : bootJobsRegister){
			job.start();
		}
	}
}
