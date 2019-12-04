package marketmaker.util;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import marketmaker.server.pojo.event.Event;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class CacheManager {

    private static CacheManager instance;

    private BlockingQueue<Event> eventQueue;

    private Map<Integer, Set<Channel>> subscriptionList;

    public CacheManager() {
        this.eventQueue = new LinkedBlockingQueue<>();
        this.subscriptionList = new ConcurrentHashMap<>();
    }

    public BlockingQueue<Event> getEventQueue() {
        return eventQueue;
    }

    public void push(Event e) {
        this.eventQueue.add(e);
    }

    public Map<Integer, Set<Channel>> getSubscriptionList() {
        return subscriptionList;
    }

    public void addSocketChannel(Integer key, Channel channel) {
       if (this.subscriptionList.get(key) == null) {
           Set<Channel> socketChannels = new HashSet<>();
           socketChannels.add(channel);
           subscriptionList.put(key, socketChannels);
       } else {
           Set<Channel> socketChannels = this.subscriptionList.get(key);
           if (socketChannels.contains(channel))
               return;
           socketChannels.add(channel);
           this.subscriptionList.put(key, socketChannels);
       }
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                instance = new CacheManager();
            }
        }
        return instance;
    }
}
