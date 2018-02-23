package secfox.soc.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.bootjobs.BootJobManager;
import secfox.soc.netty.server.handler.HttpServerHandler;

/**
 * netty server
 * 
 * @author wangzhijie
 * 
 */
public class HttpServer {

	private final static Logger logger = LoggerFactory
			.getLogger(HttpServer.class);

	static {
		try {
			PropertyConfigurator.configure(NettyConfig.getInstance()
					.getPropertyFilePath("log4j.properties"));
			logger.info("load log4j from : "
					+ NettyConfig.getInstance().getPropertyFilePath(
							"log4j.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		String host = NettyConfig.getInstance().getConfigValue(
				NettyConfig.KEY_SERVER_HOST);
		int port = 8090;
		try {
			port = Integer.parseInt(NettyConfig.getInstance().getConfigValue(
					NettyConfig.KEY_SERVER_PORT));
		} catch (Exception e) {
			port = 8090;
		}
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)/*.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)*/
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(new HttpServerCodec());
							ch.pipeline().addLast(
									new HttpObjectAggregator(8084));
							ch.pipeline().addLast(new HttpServerHandler());
						}
					});
			
			ChannelFuture f = b.bind(host, port).sync();
			hook();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	private void hook(){
		//do some other things while after start server
		new BootJobManager().start();
	}

	public static void main(String[] args) throws Exception {
		new HttpServer().start();
	}
}
