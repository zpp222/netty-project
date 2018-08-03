package org.example.demo3;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * ByteToMessageDecoder是一个ChannelInboundHandler的实现，它使得处理碎片问题变得很容易。
 * 
 * @author zpp
 *
 */
public class TimeDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {// ByteToMessageDecoder在收到新数据时使用内部维护的累积缓冲区调用decode()方法。
		if (in.readableBytes() < 4) {
			return; // decode()可以决定在累积缓冲区中没有足够的数据时不添加任何内容。当接收到更多数据时，ByteToMessageDecoder将再次调用decode()。
		}
		out.add(in.readBytes(4)); // 如果decode()向out添加一个对象，则表示解码方成功解码了一条消息。ByteToMessageDecoder将丢弃累计缓冲区的read部分。请记住，您不需要解码多个消息。ByteToMessageDecoder将继续调用decode()方法，直到没有向外添加任何内容。
	}

}
