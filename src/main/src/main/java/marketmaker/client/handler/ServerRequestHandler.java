package marketmaker.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import marketmaker.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerRequestHandler extends SimpleChannelInboundHandler<byte[]> {

    private Logger log = LoggerFactory.getLogger(ServerRequestHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext){
        log.info("Ready to send message to quote server.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
        cause.printStackTrace();
        channelHandlerContext.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        /**
         *  Decision making here, while receiving quote price from server
         */
        log.info("Received message from server: {}", MessageUtil.bytesToDouble(bytes));
    }
}
