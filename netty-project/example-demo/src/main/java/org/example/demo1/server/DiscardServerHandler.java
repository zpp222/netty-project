package org.example.demo1.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handles a server-side channel.
 * 
 * @author zpp
 *
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// Discard the received data silently.
		try {
			// ((ByteBuf) msg).release();
			// ByteBuf in = (ByteBuf) msg;
			// while (in.isReadable()) { // (1)
			// System.out.print((char) in.readByte());
			// System.out.flush();
			// }
			ctx.write(msg);
			ctx.flush();
			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}

}
