package secfox.soc.netty.server.bootjobs;

public interface IBootManager {

	public void register(IBootJob job);
	public void remove(IBootJob job);
}
