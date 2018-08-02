package org.example.demo2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception { // 当建立连接并准备生成通信量时，会调用channelActive()方法
		final ByteBuf time = ctx.alloc().buffer(4);// 要发送新消息，我们需要分配一个包含消息的新缓冲区。我们要写一个32位的整数，因此我们需要一个字节，它的容量至少为4字节。通过ChannelHandlerContext.alloc()获取当前的ByteBufAllocator并分配一个新的缓冲区。
		time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
		final ChannelFuture f = ctx.writeAndFlush(time); // ChannelFuture表示尚未发生的I/O操作。这意味着，可能还没有执行任何被请求的操作，因为所有操作在Netty中都是异步的
		f.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				assert f == future;
				ctx.close(); // close()也可能不会立即关闭连接，并返回一个ChannelFuture
			}
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
