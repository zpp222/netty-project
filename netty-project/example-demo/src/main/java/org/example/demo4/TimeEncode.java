package org.example.demo4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TimeEncode extends ChannelOutboundHandlerAdapter {
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
		System.out.println("TimeEncode.write");
		UnixTime m = (UnixTime) msg;
		ByteBuf encoded = ctx.alloc().buffer(4);
		encoded.writeInt((int) m.value());
		ctx.write(encoded, promise); // (1)
	}

}
