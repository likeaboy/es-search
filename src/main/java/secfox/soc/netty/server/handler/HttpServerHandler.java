package secfox.soc.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.response.ESQueryResponse;
import secfox.soc.netty.IBaseResponse;
import secfox.soc.netty.WriteResponseFailedException;

import com.alibaba.fastjson.JSON;

/**
 * 请求的具体处理逻辑
 * @author wangzhijie
 *
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

	private final static Logger logger = LoggerFactory
			.getLogger(HttpServerHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg)
			throws Exception {
		if (msg instanceof FullHttpRequest) {
			FullHttpRequest fullReq = (FullHttpRequest) msg;

			logger.info("[recive request, request method : "
					+ fullReq.method().name() + ",url : " +fullReq.getUri()+"] [from addr : "
					+ ctx.channel().remoteAddress() + "]");
			
			//smart
			IBaseResponse esqResp = HttpDispatcherController.getInstance().route(fullReq);
			
			ByteBuf content = Unpooled.copiedBuffer(JSON.toJSONString(esqResp), CharsetUtil.UTF_8);
			DefaultFullHttpResponse response = new DefaultFullHttpResponse(
					HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
					content.readableBytes());
			
			ChannelFuture f = ctx.channel().writeAndFlush(response);
			 f.addListener(new ChannelFutureListener() {
		            @Override
		            public void operationComplete(ChannelFuture future) {
		                if (future.isSuccess()) {
		                	String channelInfo = future.channel().toString();
		                	//close channel
		                	future.channel().close();
		                    logger.info("["+channelInfo+" done,response write success]");
		                } else {
		                    throw new WriteResponseFailedException("Failed to write response,becase future is not success,channel error.");
		                }
		                Throwable cause = future.cause();
		                if (cause != null) {
		                    throw new WriteResponseFailedException(cause);
		                }
		            }
		        });
		} else {
			logger.info("[recive request, but no handle] [reason:request is not type of FullHttpRequest, please check http client.]");
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// System.out.println("handler add");
		super.handlerAdded(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// System.out.println("channel registed");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// System.out.println("channel active");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// System.out.println("channel inactive");
		super.channelInactive(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// System.out.println("channel unregisted");
		super.channelUnregistered(ctx);
	}
}