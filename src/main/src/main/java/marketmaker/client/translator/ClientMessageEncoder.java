package marketmaker.client.translator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import marketmaker.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMessageEncoder extends MessageToByteEncoder<String> {

    private Logger log = LoggerFactory.getLogger(ClientMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) {
        try {
            //log.info("Start to encode message: {}", msg);
            out.writeBytes(MessageUtil.stringToByteArray(msg));
        } catch(Exception e) {
            log.error("Error found during encode.", e);
        } finally {

        }
    }
}
