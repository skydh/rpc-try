package provider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import pubinterface.impl.HelloInterfaceImpl;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		System.out.println("有远程调用");
		String result = new HelloInterfaceImpl().getResult(msg + "");
		ctx.writeAndFlush(result);
		System.out.println("成功返回");

	}
}