package org.example.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Discards any incoming data.
 * 
 * @author zpp
 *
 */
public class TimeServer {
	private int port;

	public TimeServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // 是一个处理I/O操作的多线程事件循环，通常称为“boss”，接受传入的连接
		EventLoopGroup workerGroup = new NioEventLoopGroup();// 通常称为“worker”，在boss接受连接并向worker注册已接受的连接时，处理已接受连接的通信量
		try {
			ServerBootstrap b = new ServerBootstrap(); // 是一个帮助器类，用于设置服务器。可以使用通道直接设置服务器。但是，请注意，这是一个冗长的过程，在大多数情况下不需要这样做。
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // 在这里，我们指定使用NioServerSocketChannel类，它用于实例化一个新的通道来接受传入连接。
					.childHandler(new ChannelInitializer<SocketChannel>() {
						/**
						 * 这里指定的处理程序将始终由新接受的通道进行计算。ChannelInitializer是一个特殊的处理程序，用于帮助用户配置一个新的通道。
						 * 您很可能希望通过添加一些处理程序(例如DiscardServerHandler)来配置新通道的通道管道，以实现您的网络应用程序。随着应用程序变得复杂，您可能会向管道中添加更多的处理程序，并最终将这个匿名类提取到顶级类中。
						 */
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new TimeServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128) // 通道实现的参数
					.childOption(ChannelOption.SO_KEEPALIVE, true); //

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync(); // 绑定到端口并启动服务器

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 9080;
		}
		new TimeServer(port).run();
	}
}
