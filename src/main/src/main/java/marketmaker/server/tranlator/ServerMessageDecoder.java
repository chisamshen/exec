package marketmaker.server.tranlator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import marketmaker.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServerMessageDecoder extends ByteToMessageDecoder {

    private Logger log = LoggerFactory.getLogger(ServerMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        try {
            if (buffer.readableBytes() > 0) {
                byte[] bytesReady = new byte[buffer.readableBytes()];
                buffer.readBytes(bytesReady);
                String result = MessageUtil.bytesToString(bytesReady);
                //log.info("Start to decode message: {}", result);
                out.add(result);
            }
        } catch (Exception e) {
            log.error("Error found during decode.", e);
        } finally {

        }
    }
}
