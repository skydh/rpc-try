package consumer;

import java.lang.reflect.Proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import pubinterface.HelloInterface;

/**
 * 怎么玩呢。根据service创建一个代理类，这个代理类通过netty去调用远程的服务即可。
 * 
 * @author Lenovo
 *
 */
public class ClientNetty {

	static HelloClientHandler aaa = new HelloClientHandler();

	public static void main(String[] args) {

		HelloInterface temp = (HelloInterface) createProxy(HelloInterface.class);
		Thread th = Thread.currentThread();

		System.out.println("Tread name(main):" + th.getName());

		System.out.println(temp.getResult("hello"));
		System.out.println(temp.getAAA("hello"));

	}

	public static Object createProxy(final Class<?> serviceClass) {

		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { serviceClass },
				(proxy, method, args) -> {
					netty(args[0] + "");

					return aaa.getResult();
				});
	}

	public static void netty(String args) {

		aaa.setPara(args);
		Bootstrap bootstrap = new Bootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();
		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				// ch.pipeline().addLast(new LifeCyCleTestHandler());
				ch.pipeline().addLast(new StringDecoder());
				ch.pipeline().addLast(new StringEncoder());
				ch.pipeline().addLast(aaa);

			}
		});

		connect(bootstrap, "127.0.0.1", 8000);
	}

	public static void connect(Bootstrap bootstrap, String host, int port) {
		bootstrap.connect(host, port).addListener(future -> {
			if (future.isSuccess()) {
			} else {
				connect(bootstrap, host, port);

			}
		});

	}

}
