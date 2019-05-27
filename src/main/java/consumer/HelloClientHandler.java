package consumer;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HelloClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public synchronized void channelActive(ChannelHandlerContext ctx) throws InterruptedException {
		System.out.println(new Date() + ": 客户端写出数据");
		// 1. 获取数据
		ByteBuf buffer = getByteBuf(ctx);
		// 2. 写数据
		ctx.channel().writeAndFlush(buffer);
		Thread th = Thread.currentThread();

		System.out.println("Tread name(channelActive):" + th.getName());
	}

	private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
		// 1. 获取二进制抽象 ByteBuf
		ByteBuf buffer = ctx.alloc().buffer();

		// 2. 准备数据，指定字符串的字符集为 utf-8
		byte[] bytes = para.getBytes(Charset.forName("utf-8"));

		// 3. 填充数据到 ByteBuf
		buffer.writeBytes(bytes);

		return buffer;
	}

	private String result;
	private String para;

	/**
	 * 收到服务端数据，唤醒等待线程
	 */
	@Override
	public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {

		setResult(msg.toString());
		notifyAll();

	}

	void setPara(String para) {
		this.para = para;
	}

	public synchronized String getResult() throws InterruptedException {
		Thread th = Thread.currentThread();

		System.out.println("Tread name(getResult):" + th.getName());
		wait();

		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}