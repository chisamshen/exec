package marketmaker.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import marketmaker.server.pojo.SimpleMessage;
import marketmaker.server.service.QuoteCalculationEngine;
import marketmaker.server.service.impl.SimpleQuoteCalculationEngine;
import marketmaker.util.CacheManager;
import marketmaker.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRequestHandler extends ChannelInboundHandlerAdapter {

    private Logger log = LoggerFactory.getLogger(SimpleRequestHandler.class);

    private QuoteCalculationEngine quoteCalculationEngine = new SimpleQuoteCalculationEngine();

    private CacheManager cacheManager = CacheManager.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object source) throws Exception {
        String msg = source.toString();
        log.info("Received message from client: {}", msg);
        SimpleMessage simpleMessage = MessageUtil.convertToMessage(msg);
        cacheManager.addSocketChannel(simpleMessage.getSecurityId(), ctx.channel());
        Double quotePrice = quoteCalculationEngine.askQuotePrice(simpleMessage.getSecurityId(), simpleMessage.isBuy(), simpleMessage.getQuantity());
        ctx.writeAndFlush(quotePrice);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client connected, address={}", ctx.channel().remoteAddress());
    }
}
