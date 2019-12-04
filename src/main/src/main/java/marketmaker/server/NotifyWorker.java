package marketmaker.server;

import io.netty.channel.Channel;
import marketmaker.server.pojo.event.QuotePriceChangeEvent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class NotifyWorker<Event> implements Runnable {

    private BlockingQueue<Event> eventQueue;

    private Map<Integer, Set<Channel>> subscriptionListMap;

    private volatile boolean isRunning;

    @Override
    public void run() {
        while (isRunning) {
            try {
                QuotePriceChangeEvent event = (QuotePriceChangeEvent) eventQueue.take();
                Set<Channel> subscriptionList = subscriptionListMap.get(event.getSecurityId());
                if (subscriptionList != null) {
                    for (Channel channel : subscriptionList) {
                        channel.writeAndFlush(event.getReferencePrice());
                    }
                }
            } catch (InterruptedException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void setEventQueue(BlockingQueue<Event> eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void setSubscriptionListMap(Map<Integer, Set<Channel>> subscriptionListMap) {
        this.subscriptionListMap = subscriptionListMap;
    }

    public void startWorker() {
        this.isRunning = true;
    }

    public void stopWorker() {
        this.isRunning = false;
    }
}
