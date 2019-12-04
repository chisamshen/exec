package marketmaker.server.tranlator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import marketmaker.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMessageEncoder extends MessageToByteEncoder<Double> {

    private Logger log = LoggerFactory.getLogger(ServerMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Double msg, ByteBuf out) {
        try {
            //log.info("Start to encode message: {}", msg);
            out.writeBytes(MessageUtil.doubleToBytes(msg));
        } catch(Exception e) {
            log.error("Error found during encode.", e);
        } finally {

        }
    }
}
