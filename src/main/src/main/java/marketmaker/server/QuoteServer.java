package marketmaker.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import marketmaker.server.pojo.event.Event;
import marketmaker.util.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.net.InetSocketAddress;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("marketmaker.server")
public class QuoteServer {

    private Logger log = LoggerFactory.getLogger(QuoteServer.class);

    @Value("${server.hostname}")
    private String hostname = "localhost";

    @Value("${server.port}")
    private int port = 7770;

    private volatile boolean isRunning = false;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static CacheManager cacheManager = CacheManager.getInstance();

    public void startServer(){
        ChannelFuture channelFuture = null;
        try{
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(new InetSocketAddress(hostname, port));
            serverBootstrap.childHandler(new ServerChannelInitializer());
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            channelFuture = serverBootstrap.bind().sync();
            if(channelFuture.isSuccess()){
                log.info("Server started successfully.");
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException ie) {
                log.error("Exception found: ", ie);
            }

        }
    }

    public synchronized void stopServer() {
        if (!this.isRunning) {
            throw new IllegalStateException("Fail to start server");
        }
        this.isRunning = false;
        try {
            Future<?> future = this.workerGroup.shutdownGracefully().await();
            if (!future.isSuccess()) {
                log.error("Fail to stop worker group: " + future.cause());
            }

            future = this.bossGroup.shutdownGracefully().await();
            if (!future.isSuccess()) {
                log.error("Fail to stop worker group: " + future.cause());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Server stop successfully.");
    }

    public static void main(String[] args) {
        SpringApplication.run(QuoteServer.class, args);
        QuoteServer server = new QuoteServer();
        NotifyWorker<Event> worker = new NotifyWorker<>();
        worker.setEventQueue(cacheManager.getEventQueue());
        worker.setSubscriptionListMap(cacheManager.getSubscriptionList());
        worker.startWorker();
        Thread workerThread = new Thread(worker);
        workerThread.start();
        server.startServer();

    }
}