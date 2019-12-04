package marketmaker.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import marketmaker.server.handler.SimpleRequestHandler;
import marketmaker.server.handler.QuoteRequestHandler;
import marketmaker.server.tranlator.ServerMessageDecoder;
import marketmaker.server.tranlator.ServerMessageEncoder;


import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     *
     * @param socketChannel
     * @throws Exception
     */

    static final EventExecutorGroup group = new DefaultEventExecutorGroup(2);

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("IdleStateHandler",
                new IdleStateHandler(15, 0, 0, TimeUnit.MINUTES));
        pipeline.addLast(
                new ServerMessageDecoder(),
                new ServerMessageEncoder()
        );
        pipeline.addLast("SimpleRequestHandler",new SimpleRequestHandler());
        /**
         *  QuoteRequestHandler should be more complicated and used for handling multiple quote requests
         */
        //pipeline.addLast(group,"QuoteRequestHandler",new QuoteRequestHandler());
    }
}
