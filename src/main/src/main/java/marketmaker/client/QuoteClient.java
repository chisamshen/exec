package marketmaker.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import marketmaker.client.handler.ServerRequestHandler;
import marketmaker.client.translator.ClientMessageDecoder;
import marketmaker.client.translator.ClientMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.net.InetSocketAddress;

@EnableAutoConfiguration
public class QuoteClient {

    private Logger log = LoggerFactory.getLogger(QuoteClient.class);

    private EventLoopGroup group = new NioEventLoopGroup();

    @Value("${server.hostname}")
    private String hostname = "localhost";

    @Value("${server.port}")
    private int port = 7770;

    private SocketChannel channel;

    public void startClient() {
        try {
            Bootstrap b = new Bootstrap();
            b.remoteAddress(new InetSocketAddress(hostname, port));
            b.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(
                                    new ClientMessageDecoder(),
                                    new ClientMessageEncoder()
                            );
                            pipeline.addLast(new ServerRequestHandler());
                            channel = ch;
                        }
                    });
            final ChannelFuture future = b.connect().sync();

            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture arg0) throws Exception {
                    if (future.isSuccess()) {
                        log.info("Connected to server successfully");

                    } else {
                        log.error("Failed to connect to server.");
                        future.cause().printStackTrace();
                        group.shutdownGracefully();
                    }
                }
            });
            log.info("Client started successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        QuoteClient client = new QuoteClient();
        client.startClient();
        String securityId = args[0];
        client.channel.writeAndFlush(securityId + " buy 100");
        Thread.sleep(10000);
    }
}